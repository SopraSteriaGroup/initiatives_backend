package com.soprasteria.initiatives.commons.api;

import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Map;

/**
 * Convertisseur de token JWT en Authentication. Permet également d'ajouter le token aux paramètres afin de pouvoir le renseigner dans
 * {@link AuthenticatedUser} par la suite
 *
 * @author jntakpe
 */
public class CustomJwtAccessTokenConverter extends JwtAccessTokenConverter {

    @Override
    protected Map<String, Object> decode(String token) {
        Map<String, Object> decode = super.decode(token);
        decode.put(SecurityConstants.TOKEN_KEY, token);
        return decode;
    }
}
