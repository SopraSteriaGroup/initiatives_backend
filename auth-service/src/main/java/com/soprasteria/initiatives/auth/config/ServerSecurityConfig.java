package com.soprasteria.initiatives.auth.config;

import com.soprasteria.initiatives.auth.web.ApiConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * General Spring Security configuration
 *
 * @author jntakpe
 * @author cegiraud
 */
@Configuration
public class ServerSecurityConfig extends WebSecurityConfigurerAdapter {

    private SSOAuthenticationProvider ssoAuthenticationProvider;

    public ServerSecurityConfig(SSOAuthenticationProvider ssoAuthenticationProvider) {
        this.ssoAuthenticationProvider = ssoAuthenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(ApiConstants.TOKENS).permitAll()
                .antMatchers("/v2/api-docs/**").permitAll()
                .antMatchers("/configuration/security").permitAll()
                .antMatchers("/configuration/ui").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .anyRequest().authenticated()
        ;
    }

}