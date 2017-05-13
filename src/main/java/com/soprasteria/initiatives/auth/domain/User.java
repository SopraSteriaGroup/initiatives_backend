package com.soprasteria.initiatives.auth.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * User entity
 *
 * @author jntakpe
 * @author cegiraud
 */
public class User extends IdentifiableEntity {

    @Indexed(unique = true)
    private String username;

    private String firstName;

    private String lastName;

    @NotNull
    @Pattern(regexp = "^.*@soprasteria.com$")
    private String email;

    private String temporaryCode;

    private Set<Authority> authorities = new HashSet<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTemporaryCode() {
        return temporaryCode;
    }

    public void setTemporaryCode(String temporaryCode) {
        this.temporaryCode = temporaryCode;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("username", username)
                .toString();
    }
}
