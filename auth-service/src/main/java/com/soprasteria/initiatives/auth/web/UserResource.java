package com.soprasteria.initiatives.auth.web;

import com.soprasteria.initiatives.auth.domain.Authority;
import com.soprasteria.initiatives.auth.domain.User;
import com.soprasteria.initiatives.auth.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

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
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public User find(@PathVariable Long userId) {
        return userService.findById(userId);
    }

    @PutMapping(ApiConstants.ACTIVATE)
    public void activate(String uuid) {
        userService.activate(uuid);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User create(@RequestBody @Valid User user) {
        return userService.create(user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}")
    public void remove(@PathVariable Long userId) {
        userService.delete(userId);
    }

    @GetMapping("/{userId}/authorities")
    public Set<Authority> findAuthorities(@PathVariable Long userId) {
        return userService.findAuthorities(userId);
    }

    @PutMapping("/{userId}/authorities/{authorityId}")
    public User addAuthority(@PathVariable Long userId, @PathVariable Long authorityId) {
        return userService.addAuthority(userId, authorityId);
    }

    @DeleteMapping("/{userId}/authorities/{authorityId}")
    public User removeAuthority(@PathVariable Long userId, @PathVariable Long authorityId) {
        return userService.removeAuthority(userId, authorityId);
    }

}
