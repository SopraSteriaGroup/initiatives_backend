package com.soprasteria.initiatives.auth.web;

import com.soprasteria.initiatives.auth.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;
import sun.security.util.SecurityConstants;

import javax.servlet.http.HttpServletRequest;

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
    public ResponseEntity<OAuth2AccessToken> authorize(@RequestHeader String authorization, HttpServletRequest request) {
        String accessToken = StringUtils.substringAfter(authorization, OAuth2AccessToken.BEARER_TYPE + " ");
        return tokenService.authorize(accessToken, request.getRequestURL().toString());
    }

}