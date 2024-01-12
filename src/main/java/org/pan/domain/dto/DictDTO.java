package org.pan.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
* @author Sinkiang
* @date 2019-04-10
*/
@Getter
@Setter
@ToString
public class DictDTO extends BaseDTO implements Serializable {

    private Long id;

    private List<DictDetailDTO> dictDetails;

    private String name;

    private String description;
}
