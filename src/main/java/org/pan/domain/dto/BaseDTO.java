package org.pan.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

/**
 * @author Sinkiang
 * @date 2019年10月24日20:48:53
 */
@Getter
@Setter
public class BaseDTO implements Serializable {

    private String createBy;

    private String updatedBy;

    private Timestamp createTime;

    private Timestamp updateTime;

    @Override
    public String toString() {
        return "BaseDTO{" +
                "createBy='" + createBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
