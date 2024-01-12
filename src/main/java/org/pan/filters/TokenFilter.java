package org.pan.filters;

import com.alibaba.fastjson2.JSON;
import org.pan.security.config.bean.SecurityProperties;
import org.pan.security.config.feign.SecurityFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Security;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class TokenFilter extends GenericFilterBean {
    private static final Logger log = LoggerFactory.getLogger(TokenFilter.class);


    private final SecurityProperties properties;

    private final SecurityFeign securityFeign;


    public TokenFilter(SecurityProperties properties,SecurityFeign securityFeign) {
        this.properties = properties;
        this.securityFeign = securityFeign;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        //1.获得token
        //2.token为null时不执行逻辑
        //3.token不为null时调用远程服务获取Authentication，放到SecurityContextHolder.getContext()中
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = resolveToken(httpServletRequest);
        // 对于 Token 为空的不需要去查 Redis
        if (StringUtils.hasText(token)) {
            //调用远程接口获取Authentication
            ResponseEntity<List<String>>  feignResult = null;
            feignResult  = securityFeign.getAuthenticationByToken();
            List<String> list = feignResult.getBody();
            list=list!=null?list:new ArrayList<>(0);
            UsernamePasswordAuthenticationToken result = UsernamePasswordAuthenticationToken
                    .authenticated("anyone",null,list.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList()));
            SecurityContextHolder.getContext().setAuthentication(result);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 初步检测Token
     *
     * @param request /
     * @return /
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(properties.getHeader());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(properties.getTokenStartWith())) {
            // 去掉令牌前缀
            return bearerToken.replace(properties.getTokenStartWith(), "");
        } else {
            log.debug("非法Token：{}", bearerToken);
        }
        return null;
    }
}
