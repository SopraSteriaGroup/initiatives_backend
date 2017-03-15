package com.soprasteria.initiatives.auth.repository;

import com.soprasteria.initiatives.auth.domain.Authority;

import java.util.Optional;

/**
 * Repository managing {@link Authority} entity
 *
 * @author jntakpe
 */
public interface AuthorityRepository extends GenericRepository<Authority> {

    Optional<Authority> findByNameIgnoreCase(String name);

}
