package com.soprasteria.initiatives.auth.repository;

import com.soprasteria.initiatives.auth.domain.Authority;
import reactor.core.publisher.Mono;

/**
 * Repository managing {@link Authority} entity
 *
 * @author jntakpe
 */
public interface AuthorityRepository extends GenericRepository<Authority> {

    Mono<Authority> findByNameIgnoreCase(String name);
}
