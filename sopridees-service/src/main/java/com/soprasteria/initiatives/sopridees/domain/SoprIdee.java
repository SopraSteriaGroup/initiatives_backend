package com.soprasteria.initiatives.sopridees.domain;

import javax.persistence.*;
import java.util.List;

/**
 * Entity représentant une SoprIdée
 *
 * @author rjansem
 */
@Entity(name = "sopridee")
public class SoprIdee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "titre")
    private String titre;

    @Column(name = "resume")
    private String resume;

    @Column(name = "idporteur")
    private Long idPorteur;

    public void setIdPorteur(Long idPorteur) {
        this.idPorteur = idPorteur;
    }

    public Long getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

}
