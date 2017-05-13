package com.soprasteria.initiatives.auth.config;

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
        AuthenticatedUser user = new AuthenticatedUser(getUsername(map), getFistName(map), getLastName(map), authorities);
        return new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(), authorities);
    }

    private String getUsername(Map<String, ?> map) {
        return (String) map.get(USERNAME);
    }

    private String getFistName(Map<String, ?> map) {
        return (String) map.get("firstName");
    }

    private String getLastName(Map<String, ?> map) {
        return (String) map.get("lastName");
    }

}
