package com.soprasteria.initiatives.auth.web;

import com.soprasteria.initiatives.auth.service.UserActivationService;
import com.soprasteria.initiatives.auth.utils.SSOProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

/**
 * Ressource permettant de gérer les utilisateurs
 *
 * @author rjansem
 * @author cegiraud
 */
@RestController
@RequestMapping(ApiConstants.AUTHENTICATION)
public class AuthenticationResource {

    private final UserActivationService userActivationService;

    public AuthenticationResource(UserActivationService userActivationService) {
        this.userActivationService = userActivationService;
    }

    @PostMapping(ApiConstants.TOKENS)
    public Mono<ResponseEntity<OAuth2AccessToken>> authorize(String accessToken, @RequestParam(defaultValue = "linkedin") String ssoProvider) {
        return userActivationService.authorize(accessToken, SSOProvider.fromString(ssoProvider))
                .map(ResponseEntity::ok)
                .onErrorMap(e -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage()));
    }

    @PostMapping(ApiConstants.SUBSCRIBE)
    public Mono<ResponseEntity<OAuth2AccessToken>> souscrire(String accessToken, @RequestParam(defaultValue = "linkedin") String ssoProvider, String email) {
        return userActivationService.souscrire(accessToken, SSOProvider.fromString(ssoProvider), email)
                .map(ResponseEntity::ok)
                .onErrorMap(e -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage()));
    }


    @PutMapping(ApiConstants.ACTIVATE)
    public Mono<Void> activate(String uuid) {
        return userActivationService.activate(uuid);
    }
}
