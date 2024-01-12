package org.pan.controller;

import org.pan.security.config.dto.AuthUserDTO;
import org.pan.security.config.feign.SecurityFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {
    @Autowired
    SecurityFeign securityFeign;
    //todo 有无效token时会出错，一会解决
    @RequestMapping("/login")
    public ResponseEntity<Object> login(@Validated @RequestBody AuthUserDTO authUser) throws Exception {
        //1.通过账号密码获取token
        ResponseEntity<Object> response = securityFeign.login(authUser);
        return response;
    }
    @RequestMapping("/checkToken")
    @PreAuthorize("@PermissionService.check('')")
    public ResponseEntity<Boolean> checkToken() throws Exception {
        //1.通过账号密码获取token
        return ResponseEntity.ok(true);
    }
}
