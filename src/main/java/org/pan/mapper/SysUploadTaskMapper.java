package org.pan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.pan.domain.entity.SysUploadTask;

/**
 * 分片上传-分片任务记录(SysUploadTask)表数据库访问层
 *
 * @since 2022-08-22 17:47:29
 */
@Mapper
public interface SysUploadTaskMapper extends BaseMapper<SysUploadTask> {

}
