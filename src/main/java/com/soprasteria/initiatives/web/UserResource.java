package com.soprasteria.initiatives.web;

import com.soprasteria.initiatives.domain.Authority;
import com.soprasteria.initiatives.domain.User;
import com.soprasteria.initiatives.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * Resource publishing {@link User} entity
 *
 * @author jntakpe
 * @author cegiraud
 */
@RestController
@RequestMapping(ApiConstants.USERS)
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Flux<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public Mono<User> find(@PathVariable String userId) {
        return userService.findById(userId);
    }
    

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<User> create(@RequestBody @Valid User user) {
        return userService.create(user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public Mono<Void> remove(@PathVariable String userId) {
        return userService.delete(userId);
    }

    @GetMapping("/{userId}/authorities")
    public Flux<Authority> findAuthorities(@PathVariable String userId) {
        return userService.findAuthorities(userId);
    }

    @PutMapping("/{userId}/authorities/{authorityId}")
    public Mono<User> addAuthority(@PathVariable String userId, @PathVariable String authorityId) {
        return userService.addAuthority(userId, authorityId);
    }

    @DeleteMapping("/{userId}/authorities/{authorityId}")
    public Mono<User> removeAuthority(@PathVariable String userId, @PathVariable String authorityId) {
        return userService.removeAuthority(userId, authorityId);
    }

}
