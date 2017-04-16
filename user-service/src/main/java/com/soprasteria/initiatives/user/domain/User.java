package com.soprasteria.initiatives.user.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Entity gérant un utilisateur
 *
 * @author rjansem
 * @author cegiraud
 */
@Entity(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    @NotNull
    @Pattern(regexp = "^.*@soprasteria.com$")
    @Column(name = "email")
    private String email;

    @Column(name = "code")
    private String codeTemporaire;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getCodeTemporaire() {
        return codeTemporaire;
    }

    public void setCodeTemporaire(String codeTemporaire) {
        this.codeTemporaire = codeTemporaire;
    }
}
