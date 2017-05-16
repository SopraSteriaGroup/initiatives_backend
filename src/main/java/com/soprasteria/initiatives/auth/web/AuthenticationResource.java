package com.soprasteria.initiatives.auth.web;

import com.soprasteria.initiatives.auth.domain.User;
import com.soprasteria.initiatives.auth.service.TokenService;
import com.soprasteria.initiatives.auth.service.UserActivationService;
import com.soprasteria.initiatives.auth.utils.SSOProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;
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

    private final TokenService tokenService;

    private final UserActivationService userActivationService;

    public AuthenticationResource(TokenService tokenService, UserActivationService userActivationService) {
        this.tokenService = tokenService;
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
                .map(resp -> resp.build());
    }

    @PostMapping(ApiConstants.TOKENS)
    public Mono<OAuth2AccessToken> authorize(@RequestParam String accessToken,
                                             @RequestParam(defaultValue = "linkedin") String ssoProvider) {
        return Mono.just(tokenService.authorize(accessToken, SSOProvider.fromString(ssoProvider)).getBody());
    }
}
