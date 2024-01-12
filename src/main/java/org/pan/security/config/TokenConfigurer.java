package org.pan.security.config;


import lombok.RequiredArgsConstructor;
import org.pan.filters.TokenFilter;
import org.pan.security.config.bean.SecurityProperties;
import org.pan.security.config.feign.SecurityFeign;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author /
 */
@RequiredArgsConstructor
public class TokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final SecurityProperties properties;
    private final SecurityFeign securityFeign;


    @Override
    public void configure(HttpSecurity http) {
        TokenFilter customFilter = new TokenFilter(properties, securityFeign);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
