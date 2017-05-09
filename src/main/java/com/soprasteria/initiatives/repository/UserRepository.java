package com.soprasteria.initiatives.repository;

import com.soprasteria.initiatives.domain.Authority;
import com.soprasteria.initiatives.domain.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

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

    Mono<User> findByUsernameAndTemporaryCode(String username, String codeTemporaire);
}
