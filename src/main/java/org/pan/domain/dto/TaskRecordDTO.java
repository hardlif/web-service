package org.pan.domain.dto;

import cn.hutool.core.bean.BeanUtil;
import io.minio.messages.Part;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.pan.domain.entity.SysUploadTask;

import java.util.List;

@Data
@ToString
@Accessors(chain = true)
public class TaskRecordDTO extends SysUploadTask {

    /**
     * 已上传完的分片
     */
    private List<Part> exitPartList;

    public static TaskRecordDTO convertFromEntity (SysUploadTask task) {
        TaskRecordDTO dto = new TaskRecordDTO();
        BeanUtil.copyProperties(task, dto);
        return dto;
    }
}
