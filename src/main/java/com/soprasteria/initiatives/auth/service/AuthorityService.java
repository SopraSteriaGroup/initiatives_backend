package com.soprasteria.initiatives.auth.service;

import com.mongodb.MongoException;
import com.soprasteria.initiatives.auth.domain.Authority;
import com.soprasteria.initiatives.auth.repository.AuthorityRepository;
import com.soprasteria.initiatives.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.ValidationException;

/**
 * Business services for {@link Authority} entity
 *
 * @author jntakpe
 * @author cegiraud
 */
@Service
public class AuthorityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorityService.class);

    private final AuthorityRepository authorityRepository;

    private final UserRepository userRepository;

    public AuthorityService(AuthorityRepository authorityRepository, UserRepository userRepository) {
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Flux<Authority> findAll() {
        return authorityRepository.findAll()
                .doOnComplete(() -> LOGGER.debug("All authorities retrieved"));
    }

    @Transactional(readOnly = true)
    public Mono<Authority> findById(String id) {
        return authorityRepository.findById(id)
                .doOnNext(x -> LOGGER.debug("Searching authority for id : '{}'", id))
                .switchIfEmpty(Mono.error(new MongoException(String.format("Unable to retrieve authority id '%s'", id))));
    }

    @Transactional
    public Mono<Authority> create(Authority authority) {
        return checkNameAvailable(authority)
                .then(authorityRepository.save(authority))
                .doOnNext(x -> LOGGER.info("Creating authority : {}", authority));
    }

    @Transactional
    public Mono<Authority> edit(String id, Authority authority) {
        authority.setId(id);
        return checkNameAvailable(authority)
                .then(authorityRepository.save(authority))
                .doOnNext(x -> LOGGER.info("Updating authority : {}", authority));
    }

    @Transactional
    public Mono<Void> delete(String id) {
        return authorityRepository.findById(id).flatMap(
                authority -> userRepository.findByAuthorities(authority)
                        .flatMap(
                                user -> {
                                    user.getAuthorities().remove(authority);
                                    return userRepository.save(user);
                                }
                        )
                        .doOnComplete(() -> authorityRepository.delete(authority))
                        .doOnComplete(() -> LOGGER.info("Deleting authority : {}", authority))
                        .then());
    }

    @Transactional
    Mono<Authority> findDefaultOrCreate() {
        return authorityRepository.findByNameIgnoreCase(Authority.DEFAULT_AUTHORITY)
                .switchIfEmpty(create(new Authority(Authority.DEFAULT_AUTHORITY)));
    }

    private Mono<Void> checkNameAvailable(Authority authority) {
        ValidationException authorityNameAlreadyUsed = new ValidationException(String.format("Authority with name'%s' already used", authority.getName()));
        return authorityRepository.findByNameIgnoreCase(authority.getName())
                .filter(a -> !a.getId().equals(authority.getId()))
                .flatMap(x -> Mono.error(authorityNameAlreadyUsed));
    }

}
