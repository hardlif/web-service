package org.pan.controller;


import io.minio.errors.*;
import org.pan.domain.dto.Result;
import org.pan.domain.dto.TaskInfoDTO;
import org.pan.domain.param.InitTaskParam;
import org.pan.service.SysUploadTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;


/**
 * 分片上传-分片任务记录(SysUploadTask)表控制层
 *
 * @since 2022-08-22 17:47:31
 */
@RestController
@RequestMapping("/v1/minio/tasks")
public class MinioUploadTaskController {
    /**
     * 服务对象
     */
    @Resource
    private SysUploadTaskService sysUploadTaskService;


    /**
     * 获取上传进度
     * @param identifier 文件md5
     * @return
     */
    @GetMapping("/{identifier}")
    public ResponseEntity<TaskInfoDTO> taskInfo (@PathVariable("identifier") String identifier) throws InsufficientDataException, NoSuchAlgorithmException, IOException, InvalidKeyException, ExecutionException, XmlParserException, InterruptedException, InternalException {
        return ResponseEntity.ok(sysUploadTaskService.getTaskInfo(identifier));
    }

    /**
     * 创建一个上传任务
     * @return
     */
    @PostMapping
    public ResponseEntity<TaskInfoDTO> initTask (@Valid @RequestBody InitTaskParam param, BindingResult bindingResult) throws InsufficientDataException, IOException, NoSuchAlgorithmException, InvalidKeyException, ExecutionException, XmlParserException, InterruptedException, InternalException {
        return ResponseEntity.ok(sysUploadTaskService.initTask(param));
    }

    /**
     * 获取每个分片的预签名上传地址
     * @param identifier
     * @param partNumber
     * @return
     */
    @GetMapping("/{identifier}/{partNumber}")
    public ResponseEntity<String> preSignUploadUrl (@PathVariable("identifier") String identifier, @PathVariable("partNumber") Integer partNumber) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResponseEntity.ok(sysUploadTaskService.genPreSignUploadUrl(identifier,partNumber));
    }

    /**
     * 合并分片
     * @param identifier
     * @return
     */
    @PostMapping("/merge/{identifier}")
    public ResponseEntity<Object> merge (@PathVariable("identifier") String identifier) throws InsufficientDataException, NoSuchAlgorithmException, IOException, ExecutionException, InvalidKeyException, InterruptedException, XmlParserException, InternalException {
        sysUploadTaskService.merge(identifier);
        return ResponseEntity.ok(null);
    }

}
