package com.soprasteria.initiatives.auth.config;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Custom implementation of Spring Security user to add custom fields
 *
 * @author jntakpe
 * @author cegiraud
 * @see User
 */
public class CustomUser extends User {

    private String firstName;

    private String lastName;

    public CustomUser(String username, String password, String firstName, String lastName, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("username", getUsername())
                .append("firstName", getFirstName())
                .append("lastName", getLastName())
                .append("authorities", getAuthorities())
                .toString();
    }
}
