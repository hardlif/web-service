package org.pan.service.impl;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.minio.CreateMultipartUploadResponse;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListPartsResponse;
import io.minio.StatObjectArgs;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Part;
import org.pan.config.MinIOClientEx;
import org.pan.config.MinIOCustomProperties;
import org.pan.domain.dto.TaskInfoDTO;
//import org.pan.domain.dto.TaskRecordDTO;
import org.pan.domain.dto.TaskRecordDTO;
import org.pan.domain.entity.SysUploadTask;
import org.pan.domain.param.InitTaskParam;
import org.pan.mapper.SysUploadTaskMapper;
import org.pan.service.SysUploadTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 分片上传-分片任务记录(SysUploadTask)表服务实现类
 *
 * @since 2022-08-22 17:47:31
 */
@Service("sysUploadTaskService")
public class SysUploadTaskServiceImpl extends ServiceImpl<SysUploadTaskMapper, SysUploadTask> implements SysUploadTaskService {

    @Autowired
    private MinIOCustomProperties minioProperties;
    @Autowired
    private MinIOClientEx minIOClient;
    @Autowired
    SysUploadTaskMapper sysUploadTaskMapper;

    @Override
    public SysUploadTask getByIdentifier(String identifier) {
        return sysUploadTaskMapper.selectOne(new QueryWrapper<SysUploadTask>().lambda().eq(SysUploadTask::getFileIdentifier, identifier));
    }

    @Override
    public TaskInfoDTO initTask(InitTaskParam param) throws InsufficientDataException, IOException, NoSuchAlgorithmException, InvalidKeyException, XmlParserException, InternalException, ExecutionException, InterruptedException {

        Date currentDate = new Date();
        String bucketName = minioProperties.getBucket();
        String fileName = param.getFileName();
        String suffix = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
        String key = StrUtil.format("{}/{}.{}", DateUtil.format(currentDate, "YYYY-MM-dd"), IdUtil.randomUUID(), suffix);
        //设置桶的名字和key
        CreateMultipartUploadResponse response = minIOClient.createMultipartUploadAsync(bucketName, null, key, null, null).get();
        //minio生成uploadId
        String uploadId = response.result().uploadId();
        //创建一个任务
        SysUploadTask task = new SysUploadTask();
        //分片数量
        int chunkNum = (int) Math.ceil(param.getTotalSize() * 1.0 / param.getChunkSize());
        task.setBucketName(minioProperties.getBucket())
                .setChunkNum(chunkNum)
                .setChunkSize(param.getChunkSize())
                .setTotalSize(param.getTotalSize())
                .setFileIdentifier(param.getIdentifier())
                .setFileName(fileName)
                .setObjectKey(key)
                .setUploadId(uploadId);
        sysUploadTaskMapper.insert(task);
        return new TaskInfoDTO().setFinished(false).setTaskRecord(TaskRecordDTO.convertFromEntity(task)).setPath(getPath(bucketName, key));
    }

    @Override
    public String getPath(String bucket, String objectKey) {
        return StrUtil.format("{}/{}/{}", minioProperties.getEndpoint(), bucket, objectKey);
    }

    @Override
    public TaskInfoDTO getTaskInfo(String identifier) throws InsufficientDataException, NoSuchAlgorithmException, IOException, InvalidKeyException, XmlParserException, InternalException, ExecutionException, InterruptedException {
        SysUploadTask task = getByIdentifier(identifier);
        if (task == null) {
            return null;
        }
        TaskInfoDTO result = new TaskInfoDTO().setTaskRecord(TaskRecordDTO.convertFromEntity(task)).setPath(getPath(task.getBucketName(), task.getObjectKey()));
        //没上传完object应该获取不到元数据，之后再研究
        boolean finished = task.isFinished();
        // 未上传完，返回已上传的分片
        if (!finished){
            //此时的task的updateId由于数据库的垃圾数据导致出错
            ListPartsResponse listPartsResponse = minIOClient.listPartsAsync(task.getBucketName(), null, task.getObjectKey(), 10000, 0, task.getUploadId(), null, null).get();
            List<Part> parts = listPartsResponse.result().partList();
            result.getTaskRecord().setExitPartList(parts);
            result.setFinished(false);
            return result;
        }

        result.setFinished(true);
        return result;
    }
    @Override
    public String genPreSignUploadUrl(String identifier, Integer partNumber) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        SysUploadTask task = getByIdentifier(identifier);
        if (task == null) {
            throw new RuntimeException("分片任务不存在");
        }
        Map<String, String> params = new HashMap<>();
        params.put("partNumber", String.valueOf(partNumber));
        params.put("uploadId", task.getUploadId());
        return minIOClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .bucket(task.getBucketName()).object(task.getObjectKey())
                .method(Method.PUT).extraQueryParams(params).build());
    }

    @Override
    public void merge(String identifier) throws ExecutionException, InterruptedException, InsufficientDataException, NoSuchAlgorithmException, IOException, InvalidKeyException, XmlParserException, InternalException {
        SysUploadTask task = getByIdentifier(identifier);
        if (task == null) {
            throw new RuntimeException("分片任务不存在");
        }
        String objectName = task.getObjectKey();
        String bucketName = task.getBucketName();
        String uploadId = task.getUploadId();

        CompletableFuture<ListPartsResponse> future = minIOClient.listPartsAsync
                (bucketName, null, objectName, 1000, 0, uploadId, null, null);
        ListPartsResponse response = future.get();
        List<Part> partList = response.result().partList();
        if (partList.size()!= task.getChunkNum()){
            // 已上传分块数量与记录中的数量不对应，不能合并分块
            throw new RuntimeException("分片缺失，请重新上传");
        }
        //todo 观察这个函数的作用,然后优化
        //向左挪了一位，感觉意义不大，而且限制了parts的大小
        Part[] parts = partList.toArray(new Part[0]);
        minIOClient.completeMultipartUploadAsync(bucketName,null,objectName,uploadId,parts,null,null);
        SysUploadTask sysUploadTask = new SysUploadTask();
        sysUploadTask.setId(task.getId());
        sysUploadTask.setFinished(true);
        sysUploadTaskMapper.updateById(sysUploadTask);
   }

    boolean isObjectExist(String bucketName, String objectName) {
        boolean exist = true;
        try {
            minIOClient.statObject(StatObjectArgs.builder().bucket(bucketName).object(objectName).build());
        } catch (Exception e) {
            exist = false;
        }
        return exist;
    }

}
