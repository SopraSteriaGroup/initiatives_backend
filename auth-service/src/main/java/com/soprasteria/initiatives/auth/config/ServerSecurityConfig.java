package com.soprasteria.initiatives.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
                .anyRequest().permitAll().and()
                .formLogin().permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .authenticationProvider(ssoAuthenticationProvider);
    }

}