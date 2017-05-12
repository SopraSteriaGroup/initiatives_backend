package com.soprasteria.initiatives.web;

import com.soprasteria.initiatives.service.TokenService;
import com.soprasteria.initiatives.utils.SSOProvider;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Resource providing simplified access to token
 *
 * @author jntakpe
 * @author cegiraud
 */
@RestController
@RequestMapping(ApiConstants.TOKENS)
public class TokenResource {

    private final TokenService tokenService;

    public TokenResource(TokenService tokenService) {
        this.tokenService = tokenService;
    }


    @PostMapping
    public Mono<OAuth2AccessToken> authorize(@RequestParam String accessToken,
                                             @RequestParam(defaultValue = "linkedin") String ssoProvider) {
        return Mono.just(tokenService.authorize(accessToken, SSOProvider.fromString(ssoProvider)).getBody());
    }
}