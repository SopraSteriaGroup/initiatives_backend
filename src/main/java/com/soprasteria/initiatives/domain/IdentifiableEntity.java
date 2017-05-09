package com.soprasteria.initiatives.domain;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * Entity containing auto generated primary key
 *
 * @author jntakpe
 */
public abstract class IdentifiableEntity implements Serializable {

    @Id
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
