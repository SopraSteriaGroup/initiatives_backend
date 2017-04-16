package com.soprasteria.initiatives.user.api;

import com.soprasteria.initiatives.user.domain.Utilisateur;
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
@RequestMapping(ApiConstants.USERS_URI)
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(ApiConstants.SOUSCRIRE_URI)
    public void souscrire(Utilisateur utilisateur) {
        userService.souscrire(utilisateur);
    }

}
