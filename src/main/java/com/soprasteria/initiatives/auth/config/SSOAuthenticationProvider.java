package com.soprasteria.initiatives.auth.config;

import com.soprasteria.initiatives.auth.domain.User;
import com.soprasteria.initiatives.auth.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SSOAuthenticationProvider implements AuthenticationProvider {

    private UserService userService;

    public SSOAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getPrincipal().toString();
        return userService.findByUsernameIgnoreCase(username)
                .map(this::buildAuthentication)
                .block();
    }


    private Authentication buildAuthentication(User user) {
        List<GrantedAuthority> authorities = user.getAuthorities()
                .stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
        AuthenticatedUser authenticatedUser =
                new AuthenticatedUser(user.getUsername(), user.getFirstName(), user.getLastName(), authorities);
        return new UsernamePasswordAuthenticationToken(authenticatedUser, "", authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}