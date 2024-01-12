package org.pan.security.config.feign;

import org.pan.security.config.dto.AuthUserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

@Component
@FeignClient(value ="cloud-permission-system")
public interface SecurityFeign {
    @RequestMapping("/remote/auth/login")
    ResponseEntity<Object> login(AuthUserDTO authUser);
    @RequestMapping("/auth/getAuthentication")
    ResponseEntity<List<String>> getAuthenticationByToken();
    @RequestMapping("/auth/getAuthentication2")
    ResponseEntity<UsernamePasswordAuthenticationToken> getAuthenticationByToken2();
}
