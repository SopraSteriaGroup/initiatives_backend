package com.soprasteria.initiatives.sopridees.api;

import com.soprasteria.initiatives.sopridees.domain.Utilisateur;
import com.soprasteria.initiatives.sopridees.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ressource permettant de gérer les utilisateurs
 *
 * @author rjansem
 */
@RestController
@RequestMapping(ApiConstants.USERS_URI)
public class UserResource {

    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(ApiConstants.SOUSCRIRE_URI)
    public void souscrire(Utilisateur utilisateur) {
        userService.souscrire(utilisateur);
    }

}
