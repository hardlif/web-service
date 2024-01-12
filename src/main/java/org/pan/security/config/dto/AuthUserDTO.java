package org.pan.security.config.dto;

import lombok.Getter;
import lombok.Setter;


/**
 * @author Sinkiang
 * @date 2018-11-30
 */
@Getter
@Setter
public class AuthUserDTO {

    private String username;

    private String password;

    private String code;

    private String uuid;

    @Override
    public String toString() {
        return "{username=" + username  + ", password= ******}";
    }
}
