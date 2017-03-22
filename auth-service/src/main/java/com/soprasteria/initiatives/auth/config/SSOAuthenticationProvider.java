package com.soprasteria.initiatives.auth.config;

import com.soprasteria.initiatives.auth.config.properties.SSOProperties;
import com.soprasteria.initiatives.auth.utils.SSOProvider;
import com.soprasteria.initiatives.commons.api.AuthenticatedUser;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Configuration
public class SSOAuthenticationProvider implements AuthenticationProvider {

    private SSOProperties ssoProperties;

    public SSOAuthenticationProvider(SSOProperties ssoProperties) {
        this.ssoProperties = ssoProperties;
    }


    @Override
    public Authentication authenticate(Authentication authentication) {
        Map details = (Map) authentication.getDetails();
        SSOProvider ssoProvider = SSOProvider.fromString((String) details.get("provider"));
        SSOProperties.SSOValues ssoValues = ssoProperties.getProviders().get(ssoProvider);
        UserSSO userSSO = callSSOProvider(authentication.getPrincipal().toString(), ssoValues);

        //TODO call MS pour reccupération des roles
        List<GrantedAuthority> authorities = AuthorityUtils.NO_AUTHORITIES;
        String userName = ssoProvider.toString() + "#" + userSSO.id;
        AuthenticatedUser authenticatedUser = new AuthenticatedUser("", userName, userSSO.firstName, userSSO.lastName, authorities);
        return new UsernamePasswordAuthenticationToken(authenticatedUser, "", authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }


    private UserSSO callSSOProvider(String accessToken, SSOProperties.SSOValues ssoValues) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, OAuth2AccessToken.BEARER_TYPE + " " + accessToken);
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = new RestTemplate()
                .exchange(ssoValues.getProfileUrl(), HttpMethod.GET, request, Map.class);
        return new UserSSO(response.getBody(), ssoValues);
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