package com.soprasteria.initiatives.web;

import com.soprasteria.initiatives.domain.User;
import com.soprasteria.initiatives.service.UserActivationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Ressource permettant de gérer les utilisateurs
 *
 * @author rjansem
 * @author cegiraud
 */
@RestController
@RequestMapping(ApiConstants.USERS)
public class UserActivationResource {

    private final UserActivationService userActivationService;

    public UserActivationResource(UserActivationService userActivationService) {
        this.userActivationService = userActivationService;
    }

    @PostMapping(ApiConstants.SUBSCRIBE)
    public Mono<Void> souscrire(User user) {
        return userActivationService.souscrire(user);
    }

    @PutMapping(ApiConstants.ACTIVATE)
    public Mono<Void> activate(String uuid) {
        return userActivationService.activate(uuid);
    }

    @GetMapping(ApiConstants.EXISTS)
    public Mono<ResponseEntity<Object>> exists() {
        return userActivationService.exist()
                .map((Boolean exist) -> exist ? ResponseEntity.ok() : ResponseEntity.notFound())
                .map(response -> response.build());
    }

}
