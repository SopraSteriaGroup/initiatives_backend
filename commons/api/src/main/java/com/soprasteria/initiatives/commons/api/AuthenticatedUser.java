package com.soprasteria.initiatives.commons.api;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

/**
 * Bean modeling authenticated user
 *
 * @author jntakpe
 */
public class AuthenticatedUser implements Principal {

    public static final AuthenticatedUser EMPTY_USER = new AuthenticatedUser("", "", "", "", Collections.emptyList());

    private final String username;

    private String firstName;

    private String lastName;

    private final Collection<? extends GrantedAuthority> authorities;

    private String authorizationHeaderField;

    public AuthenticatedUser(String token, String username, String firstName, String lastName,
                             Collection<? extends GrantedAuthority> authorities) {
        this.authorizationHeaderField = toAuthorizationHeaderField(token);
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.authorities = authorities;
    }

    public String getAuthorizationHeaderField() {
        return authorizationHeaderField;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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

    /**
     * Returns the name of this principal.
     *
     * @return the name of this principal.
     */
    @Override
    public String getName() {
        return username;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("username", username)
                .append("firstName", getFirstName())
                .append("lastName", getLastName())
                .append("authorities", getAuthorities())
                .toString();
    }

    private String toAuthorizationHeaderField(String token) {
        return SecurityConstants.BEARER_PREFIX + token;
    }
}
