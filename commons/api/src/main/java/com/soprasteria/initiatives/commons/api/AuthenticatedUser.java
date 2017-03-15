package com.soprasteria.initiatives.commons.api;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Bean modeling authenticated user
 *
 * @author jntakpe
 */
public class AuthenticatedUser {

    public static final AuthenticatedUser EMPTY_USER = new AuthenticatedUser("", "", Collections.emptyList());

    private final String token;

    private final String username;

    private final Collection<? extends GrantedAuthority> authorities;

    AuthenticatedUser(String token, String username, Collection<? extends GrantedAuthority> authorities) {
        this.token = toBearer(token);
        this.username = username;
        this.authorities = authorities;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthenticatedUser)) {
            return false;
        }
        AuthenticatedUser that = (AuthenticatedUser) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("username", username)
                .append("authorities", authorities)
                .toString();
    }

    private String toBearer(String token) {
        return SecurityConstants.BEARER_PREFIX + token;
    }
}
