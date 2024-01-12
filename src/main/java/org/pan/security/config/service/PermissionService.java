package org.pan.security.config.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.util.Set;
import java.util.stream.Collectors;

@Service("PermissionService")
public class PermissionService {
    public boolean check(String permission){
        if("".equals(permission)){
            return true;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Set<String> collect = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return collect.contains(permission);
    }
}
