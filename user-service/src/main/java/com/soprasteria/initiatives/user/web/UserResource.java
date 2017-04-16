package com.soprasteria.initiatives.user.web;

import com.soprasteria.initiatives.user.domain.User;
import com.soprasteria.initiatives.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Ressource permettant de gérer les utilisateurs
 *
 * @author rjansem
 * @author cegiraud
 */
@RestController
@RequestMapping(ApiConstants.USERS)
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(ApiConstants.SUBSCRIBE)
    public void souscrire(User user) {
        userService.souscrire(user);
    }

    @PutMapping(ApiConstants.ACTIVATE)
    public void activate(String uuid) {
        userService.activate(uuid);
    }

    @GetMapping(ApiConstants.EXISTS)
    public ResponseEntity exists() {
        return userService.exist() ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

}
