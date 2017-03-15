package com.soprasteria.initiatives.auth.web;

import com.soprasteria.initiatives.auth.domain.User;
import com.soprasteria.initiatives.auth.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Resource providing simplified access to token
 *
 * @author jntakpe
 */
@RestController
@RequestMapping(ApiConstants.TOKENS)
public class TokenResource {

    private final TokenService tokenService;

    @Autowired
    public TokenResource(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<OAuth2AccessToken> authorize(@RequestBody @Valid User user, HttpServletRequest request) {
        return tokenService.authorize(user, request.getRequestURL().toString());
    }

}
