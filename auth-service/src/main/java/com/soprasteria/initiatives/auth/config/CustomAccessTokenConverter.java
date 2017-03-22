package com.soprasteria.initiatives.auth.config;

import com.soprasteria.initiatives.commons.api.AuthenticatedUser;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom converter to handle additional JWT fields
 *
 * @author jntakpe
 * @author cegiraud
 * @see AccessTokenConverter
 */
public class CustomAccessTokenConverter extends DefaultAccessTokenConverter {

    @Override
    public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        response.putAll(super.convertAccessToken(token, authentication));
        AuthenticatedUser user = getAuthenticatedUserFromAuthentication(authentication);
        response.put("firstName", user.getFirstName());
        response.put("lastName", user.getLastName());
        return response;
    }

    private AuthenticatedUser getAuthenticatedUserFromAuthentication(OAuth2Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof AuthenticatedUser) {
            return (AuthenticatedUser) principal;
        } else {
            throw new IllegalStateException("Wrong principal type");
        }
    }
}
