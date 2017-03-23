package com.soprasteria.initiatives.auth.repository;

import com.soprasteria.initiatives.auth.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.Optional;

/**
 * Repository managing {@link User} entity
 *
 * @author jntakpe
 * @author cegiraud
 */
public interface UserRepository extends GenericRepository<User> {

    @EntityGraph(value = "User.detail", type = EntityGraph.EntityGraphType.LOAD)
    Optional<User> findByUsernameIgnoreCase(String username);

    @Override
    @EntityGraph(value = "User.detail", type = EntityGraph.EntityGraphType.LOAD)
    User findOne(Long userId);
}
