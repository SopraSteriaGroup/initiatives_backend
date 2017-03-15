package com.soprasteria.initiatives.auth.web;

import com.soprasteria.initiatives.auth.domain.Authority;
import com.soprasteria.initiatives.auth.domain.User;
import com.soprasteria.initiatives.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * Resource publishing {@link User} entity
 *
 * @author jntakpe
 */
@RestController
@RequestMapping(ApiConstants.USERS)
public class UserResource {

    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<User> findAll() {
        return userService.findAll();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public User find(@PathVariable Long userId) {
        return userService.findById(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST)
    public User create(@RequestBody @Valid User user) {
        return userService.create(user);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public User update(@PathVariable Long userId, @RequestBody @Valid User user) {
        return userService.edit(userId, user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public void remove(@PathVariable Long userId) {
        userService.delete(userId);
    }

    @RequestMapping(value = "/{userId}/authorities", method = RequestMethod.GET)
    public Set<Authority> findAuthorities(@PathVariable Long userId) {
        return userService.findAuthorities(userId);
    }

    @RequestMapping(value = "/{userId}/authorities/{authorityId}", method = RequestMethod.PUT)
    public User addAuthority(@PathVariable Long userId, @PathVariable Long authorityId) {
        return userService.addAuthority(userId, authorityId);
    }

    @RequestMapping(value = "/{userId}/authorities/{authorityId}", method = RequestMethod.DELETE)
    public User removeAuthority(@PathVariable Long userId, @PathVariable Long authorityId) {
        return userService.removeAuthority(userId, authorityId);
    }

}
