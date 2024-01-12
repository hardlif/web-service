package org.pan.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
* @author Sinkiang
* @date 2019-04-10
*/
@Getter
@Setter
@ToString
public class DictDetailDTO extends BaseDTO implements Serializable {

    private Long id;

    private DictSmallDTO dict;

    private String label;

    private String value;

    private Integer dictSort;
}
