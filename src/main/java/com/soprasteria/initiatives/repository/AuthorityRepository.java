package com.soprasteria.initiatives.repository;

import com.soprasteria.initiatives.domain.Authority;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Repository managing {@link Authority} entity
 *
 * @author jntakpe
 */
public interface AuthorityRepository extends GenericRepository<Authority> {

    Mono<Authority> findByNameIgnoreCase(String name);
}
