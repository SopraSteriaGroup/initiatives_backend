package com.soprasteria.initiatives.auth.repository;

import com.soprasteria.initiatives.auth.domain.Authority;
import com.soprasteria.initiatives.auth.domain.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repository managing {@link User} entity
 *
 * @author jntakpe
 * @author cegiraud
 */
public interface UserRepository extends GenericRepository<User> {

    Mono<User> findByUsernameIgnoreCase(String username);

    @Override
    Mono<User> findById(String id);

    Flux<User> findByAuthorities(Authority authority);

    Mono<User> findByIdSSO(String idSSO);

    Mono<User> findByUsernameAndTemporaryCode(String username, String codeTemporaire);
}
