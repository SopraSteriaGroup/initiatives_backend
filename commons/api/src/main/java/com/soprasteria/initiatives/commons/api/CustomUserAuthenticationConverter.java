package com.soprasteria.initiatives.commons.api;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.Collection;
import java.util.Map;


/**
 * Custom mapping for Spring Security's {@link User}
 *
 * @author jntakpe
 * @see DefaultUserAuthenticationConverter
 */
public class CustomUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        Authentication authentication = super.extractAuthentication(map);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        AuthenticatedUser user = new AuthenticatedUser(getToken(map), getUsername(map), authorities);
        return new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(), authorities);
    }

    private String getToken(Map<String, ?> map) {
        return (String) map.get(SecurityConstants.TOKEN_KEY);
    }

    private String getUsername(Map<String, ?> map) {
        return (String) map.get(USERNAME);
    }

}
