package com.soprasteria.initiatives.auth.service;

import com.mongodb.MongoException;
import com.soprasteria.initiatives.auth.domain.Authority;
import com.soprasteria.initiatives.auth.domain.User;
import com.soprasteria.initiatives.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.ValidationException;

/**
 * Business services for {@link User} entity
 *
 * @author jntakpe
 */
@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final AuthorityService authorityService;

    public UserService(UserRepository userRepository, AuthorityService authorityService) {
        this.userRepository = userRepository;
        this.authorityService = authorityService;
    }


    @Transactional(readOnly = true)
    public Flux<User> findAll() {
        return userRepository.findAll()
                .doOnComplete(() -> LOGGER.debug("All users retrieved"));
    }

    @Transactional(readOnly = true)
    public Mono<User> findById(String id) {
        Mono<User> errorMono = Mono.error(new MongoException(String.format("Unable to retrieve user id : '%s'", id)));
        return userRepository.findById(id)
                .doOnNext(x -> LOGGER.debug("Searching user for id : '{}'", id))
                .switchIfEmpty(errorMono);
    }


    @Transactional
    public Mono<User> create(User user) {
        return checkUsernameAvailable(user)
                .then(userRepository.save(user))
                .doOnNext(x -> LOGGER.info("Creating new user : {}", user));
    }

    @Transactional
    public Mono<Void> delete(String id) {
        return findById(id).flatMap(userRepository::delete);
    }

    @Transactional(readOnly = true)
    public Flux<Authority> findAuthorities(String userId) {
        return findById(userId).flatMapIterable(user -> {
            LOGGER.debug("Searching authorities for user : {}", user);
            return user.getAuthorities();
        });
    }

    @Transactional(readOnly = true)
    public Flux<Authority> findAuthoritiesByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username)
                .flatMapIterable(user -> {
                    LOGGER.debug("Searching authorities for user : {}", user);
                    return user.getAuthorities();
                });
    }


    @Transactional
    public Mono<User> addAuthority(String userId, String authorityId) {
        return authorityService.findById(authorityId)
                .flatMap(authority -> userRepository.findById(userId)
                        .flatMap(user -> checkAuthorityDoesntExist(user, authority)
                                .map(x -> {
                                    LOGGER.info("Adding authority {} to user {}", authority, user);
                                    user.getAuthorities().add(authority);
                                    return user;
                                })));
    }

    @Transactional
    public Mono<User> removeAuthority(String userId, String authorityId) {
        return authorityService.findById(authorityId)
                .flatMap(authority -> userRepository.findById(userId)
                        .flatMap(user -> checkAuthorityExist(user, authority)
                                .map(x -> {
                                    LOGGER.info("Removing authority {} to user {}", authority, user);
                                    user.getAuthorities().remove(authority);
                                    return user;
                                })
                        ));
    }

    private Mono<Void> checkAuthorityDoesntExist(User user, Authority authority) {
        Mono<Void> error = Mono.error(new ValidationException(String.format("User %s already has authority %s", user, authority)));
        return user.getAuthorities()
                .stream()
                .filter(a -> a.equals(authority))
                .findAny()
                .map(e -> error)
                .orElse(Mono.empty());
    }

    private Mono<?> checkAuthorityExist(User user, Authority authority) {
        Mono<Object> error = Mono.error(new ValidationException(String.format("User %s has not authority %s", user, authority)));
        return user.getAuthorities()
                .stream()
                .filter(a -> a.equals(authority))
                .findAny()
                .map(e -> Mono.empty())
                .orElse(error);
    }

    private Mono<Void> checkUsernameAvailable(User user) {
        Mono<Void> error = Mono.error(new ValidationException(String.format("Username '%s' is already used", user.getUsername())));
        return userRepository.findByUsernameIgnoreCase(user.getUsername())
                .flatMap(x -> error);
    }

}