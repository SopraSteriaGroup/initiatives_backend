package com.soprasteria.initiatives.auth.dao;

import com.soprasteria.initiatives.auth.domain.Authority;
import com.soprasteria.initiatives.auth.domain.User;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

/**
 * Bean managing join between {@link User} and {@link Authority}
 *
 * @author jntakpe
 */
public class UserIdAuthorityId {

    private final Long userId;

    private final Long authorityId;

    public UserIdAuthorityId(Long userId, Long authorityId) {
        this.userId = userId;
        this.authorityId = authorityId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getAuthorityId() {
        return authorityId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserIdAuthorityId)) {
            return false;
        }
        UserIdAuthorityId that = (UserIdAuthorityId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(authorityId, that.authorityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, authorityId);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("userId", userId)
                .append("authorityId", authorityId)
                .toString();
    }


}
