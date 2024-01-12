package org.pan.service;


import com.baomidou.mybatisplus.extension.service.IService;
import io.minio.errors.*;
import org.pan.domain.entity.SysUploadTask;
import org.pan.domain.dto.TaskInfoDTO;
import org.pan.domain.param.InitTaskParam;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 分片上传-分片任务记录(SysUploadTask)表服务接口
 *
 * @since 2022-08-22 17:47:30
 */
public interface SysUploadTaskService extends IService<SysUploadTask> {

    /**
     * 根据md5标识获取分片上传任务
      * @param identifier
     * @return
     */
    SysUploadTask getByIdentifier (String identifier);

    /**
     * 初始化一个任务
     */
    TaskInfoDTO initTask (InitTaskParam param) throws InsufficientDataException, IOException, NoSuchAlgorithmException, InvalidKeyException, XmlParserException, InternalException, ExecutionException, InterruptedException;

    /**
     * 获取文件地址
     * @param bucket
     * @param objectKey
     * @return
     */
    String getPath (String bucket, String objectKey);

    /**
     * 获取上传进度
     * @param identifier
     * @return
     */
    TaskInfoDTO getTaskInfo (String identifier) throws InsufficientDataException, NoSuchAlgorithmException, IOException, InvalidKeyException, XmlParserException, InternalException, ExecutionException, InterruptedException;



    /**
     * 合并分片
     * @param identifier
     */
    void merge (String identifier) throws ExecutionException, InterruptedException, InsufficientDataException, NoSuchAlgorithmException, IOException, InvalidKeyException, XmlParserException, InternalException;

    /**
     * 生成预签名上传url
     * @return
     */
    String genPreSignUploadUrl(String identifier, Integer partNumber) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
}
