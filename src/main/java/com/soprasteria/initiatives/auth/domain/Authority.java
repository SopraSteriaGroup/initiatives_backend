package com.soprasteria.initiatives.auth.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Authority entity
 *
 * @author jntakpe
 * @author cegiraud
 */
public class Authority extends IdentifiableEntity {

    public static final String DEFAULT_AUTHORITY = "USER";

    @NotNull
    @Indexed(unique = true)
    private String name;

    public Authority() {
        //Default constructor
    }

    public Authority(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Authority)) {
            return false;
        }
        Authority authority = (Authority) o;
        return Objects.equals(name, authority.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .toString();
    }

}
