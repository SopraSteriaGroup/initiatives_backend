package com.soprasteria.initiatives.service;

import com.soprasteria.initiatives.utils.SSOProvider;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.StringJoiner;

/**
 * Business service to obtain OAuth2 token
 *
 * @author jntakpe
 * @author cegiraud
 */
@Service
public class TokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

    private static final String BASIC_PREFIX = "Basic ";

    private static final String BLANK_WORD = "";

    private static final String OAUTH2_TOKEN_URL = "/oauth/token";

    @Value("${server.port:0}")
    private int port;

    private final OAuth2ClientProperties oAuth2ClientProperties;

    public TokenService(OAuth2ClientProperties oAuth2ClientProperties) {
        this.oAuth2ClientProperties = oAuth2ClientProperties;
    }

    public ResponseEntity<OAuth2AccessToken> authorize(String accessToken, SSOProvider ssoProvider) {
        try {
            String url = url(accessToken, ssoProvider);
            return new RestTemplate().postForEntity(url, new HttpEntity(initializeHeaders()), OAuth2AccessToken.class);
        } catch (Exception e) {
            LOGGER.warn("Unable to obtain token {}", e);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    private String url(String accessToken, SSOProvider ssoProvider) {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path(OAUTH2_TOKEN_URL)
                .queryParams(requestParams(accessToken, ssoProvider))
                .toUriString();
    }

    private MultiValueMap<String, String> requestParams(String authorization, SSOProvider ssoProvider) {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("username", authorization);
        requestParams.add("password", BLANK_WORD);
        requestParams.add(OAuth2Utils.GRANT_TYPE, "password");
        requestParams.add(OAuth2Utils.SCOPE, "openid");
        requestParams.add(OAuth2Utils.CLIENT_ID, oAuth2ClientProperties.getClientId());
        requestParams.add("secret", oAuth2ClientProperties.getClientSecret());
        requestParams.add("ssoProvider", ssoProvider.toString());
        return requestParams;
    }

    private MultiValueMap<String, String> initializeHeaders() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.AUTHORIZATION, BASIC_PREFIX + base64ClientIdSecret());
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        return headers;
    }

    private String base64ClientIdSecret() {
        String clientIdSecret = new StringJoiner(":")
                .add(oAuth2ClientProperties.getClientId())
                .add(oAuth2ClientProperties.getClientSecret()).toString();
        return Base64.encodeBase64String(clientIdSecret.getBytes());
    }
}
