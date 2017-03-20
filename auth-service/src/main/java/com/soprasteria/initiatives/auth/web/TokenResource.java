package com.soprasteria.initiatives.auth.web;

import com.soprasteria.initiatives.auth.service.TokenService;
import com.soprasteria.initiatives.auth.utils.SSOProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<OAuth2AccessToken> authorize(@RequestHeader String authorization,
                                                       @RequestParam(defaultValue = "linkedin") String ssoProvider,
                                                       HttpServletRequest request) {
        String accessToken = StringUtils.substringAfter(authorization, OAuth2AccessToken.BEARER_TYPE + " ");
        return tokenService.authorize(accessToken, SSOProvider.fromString(ssoProvider), request.getRequestURL().toString());
    }

}