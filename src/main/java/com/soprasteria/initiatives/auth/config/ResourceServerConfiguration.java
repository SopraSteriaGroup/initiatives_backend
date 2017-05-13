package com.soprasteria.initiatives.auth.config;

import com.soprasteria.initiatives.auth.web.ApiConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * General Spring Security configuration
 *
 * @author jntakpe
 * @author cegiraud
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(ApiConstants.TOKENS).permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers(SecurityConstants.SWAGGER_PATHS).permitAll()
                .anyRequest().authenticated();
    }
}