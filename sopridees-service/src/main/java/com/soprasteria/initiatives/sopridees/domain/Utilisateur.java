package com.soprasteria.initiatives.sopridees.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Entity gérant un utilisateur
 *
 * @author rjansem
 */
@Entity(name = "utilisateur")
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "idsso")
    private String idSso;

    @NotNull
    @Pattern(regexp = "^.*@soprasteria.com$")
    @Column(name = "email")
    private String email;

    @Column(name = "code")
    private String codeTemporaire;

    public Long getId() {
        return id;
    }

    public String getIdSso() {
        return idSso;
    }

    public void setIdSso(String idSso) {
        this.idSso = idSso;
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
