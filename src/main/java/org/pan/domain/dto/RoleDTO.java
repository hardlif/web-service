package org.pan.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Sinkiang
 * @date 2018-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RoleDTO extends BaseDTO implements Serializable {

    private Long id;

    private Set<MenuDTO> menus;

    private Set<DeptDTO> depts;

    private String name;

    private String dataScope;

    private Integer level;

    private String description;

}
