package com.soprasteria.initiatives.auth.repository;

import com.soprasteria.initiatives.auth.domain.User;

import java.util.Optional;

/**
 * Repository managing {@link User} entity
 *
 * @author jntakpe
 */
public interface UserRepository extends GenericRepository<User> {

    Optional<User> findByUsernameIgnoreCase(String username);

}
