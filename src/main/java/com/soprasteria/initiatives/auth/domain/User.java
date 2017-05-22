package com.soprasteria.initiatives.auth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
@Document
public class User extends IdentifiableEntity {

    @NotNull
    @Indexed(unique = true)
    @Pattern(regexp = "^.*@soprasteria.com$")
    private String username;

    private String firstName;

    private String lastName;

    @JsonIgnore
    private String temporaryCode;

    private String idSSO;

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

    public String getTemporaryCode() {
        return temporaryCode;
    }

    public void setTemporaryCode(String temporaryCode) {
        this.temporaryCode = temporaryCode;
    }

    public String getIdSSO() {
        return idSSO;
    }

    public void setIdSSO(String idSSO) {
        this.idSSO = idSSO;
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
