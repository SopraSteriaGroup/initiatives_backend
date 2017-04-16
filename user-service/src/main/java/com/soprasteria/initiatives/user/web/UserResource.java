package com.soprasteria.initiatives.user.web;

import com.soprasteria.initiatives.user.domain.User;
import com.soprasteria.initiatives.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
