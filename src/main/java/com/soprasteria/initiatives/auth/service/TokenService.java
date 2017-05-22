package com.soprasteria.initiatives.auth.service;

import com.soprasteria.initiatives.auth.config.properties.SSOProperties;
import com.soprasteria.initiatives.auth.domain.User;
import com.soprasteria.initiatives.auth.utils.SSOProvider;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.StringJoiner;

/**
 * Business service to obtain OAuth2 token
 *
 * @author jntakpe
 * @author cegiraud
 */
@Service
public class TokenService {

    private static final String BASIC_PREFIX = "Basic ";

    private static final String BLANK_WORD = "";

    private static final String OAUTH2_TOKEN_URL = "/oauth/token";

    @Value("${server.port:0}")
    private int port;

    private final OAuth2ClientProperties oAuth2ClientProperties;

    private final SSOProperties ssoProperties;

    public TokenService(OAuth2ClientProperties oAuth2ClientProperties, SSOProperties ssoProperties) {
        this.oAuth2ClientProperties = oAuth2ClientProperties;
        this.ssoProperties = ssoProperties;
    }

    public Mono<OAuth2AccessToken> authorize(String username) {
        return WebClient.builder()
                .build()
                .post()
                .uri(url(username))
                .header(HttpHeaders.AUTHORIZATION, BASIC_PREFIX + base64ClientIdSecret())
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .exchange()
                .flatMap(clientResponse -> clientResponse.bodyToMono(OAuth2AccessToken.class));
    }


    public Mono<User> callSSOProvider(String accessToken, SSOProvider ssoProvider) {
        SSOProperties.SSOValues keys = ssoProperties.getProviders().get(ssoProvider);
        return WebClient.builder()
                .build()
                .get()
                .uri(keys.getProfileUrl())
                .header(HttpHeaders.AUTHORIZATION, OAuth2AccessToken.BEARER_TYPE + " " + accessToken)
                .exchange()
                .flatMap(resp -> resp.bodyToMono(Map.class))
                .map(values -> new TokenService.UserSSO(values, keys))
                .map(userSSO -> {
                    User user = new User();
                    user.setIdSSO(ssoProvider.toString() + "#" + userSSO.id);
                    user.setFirstName(userSSO.firstName);
                    user.setLastName(userSSO.lastName);
                    return user;
                });
    }

    private String url(String username) {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path(OAUTH2_TOKEN_URL)
                .queryParams(requestParams(username))
                .toUriString();
    }

    private MultiValueMap<String, String> requestParams(String username) {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("username", username);
        requestParams.add("password", BLANK_WORD);
        requestParams.add(OAuth2Utils.GRANT_TYPE, "password");
        requestParams.add(OAuth2Utils.SCOPE, "openid");
        requestParams.add(OAuth2Utils.CLIENT_ID, oAuth2ClientProperties.getClientId());
        requestParams.add("secret", oAuth2ClientProperties.getClientSecret());
        return requestParams;
    }

    private String base64ClientIdSecret() {
        String clientIdSecret = new StringJoiner(":")
                .add(oAuth2ClientProperties.getClientId())
                .add(oAuth2ClientProperties.getClientSecret()).toString();
        return Base64.encodeBase64String(clientIdSecret.getBytes());
    }


    private static class UserSSO {

        private String id;

        private String firstName;

        private String lastName;

        UserSSO(Map values, SSOProperties.SSOValues keys) {
            id = values.get(keys.getId()).toString();
            firstName = values.get(keys.getFirstName()).toString();
            lastName = values.get(keys.getLastName()).toString();
        }
    }
}
