package com.soprasteria.initiatives.auth.config;

import com.soprasteria.initiatives.auth.config.properties.SSOProperties;
import com.soprasteria.initiatives.auth.utils.SSOProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Configuration
public class SSOAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private SSOProperties ssoProperties;

    public SSOAuthenticationProvider(SSOProperties ssoProperties) {
        this.ssoProperties = ssoProperties;
    }

    @Override
    protected final void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) {
        //not used
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) {
        Map<String, String> details = (Map<String, String>) authentication.getDetails();
        SSOProvider ssoProvider = SSOProvider.fromString(details.get("provider"));
        SSOProperties.SSOValues ssoValues = ssoProperties.getProviders().get(ssoProvider);
        User user = callSSOProvider(username, ssoValues);

        //TODO call MS pour reccupération des roles
        List<GrantedAuthority> authorities = AuthorityUtils.NO_AUTHORITIES;
        return new CustomUser(user.id, "", user.firstName, user.lastName, authorities);
    }


    private User callSSOProvider(String accessToken, SSOProperties.SSOValues ssoValues) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, OAuth2AccessToken.BEARER_TYPE + " " + accessToken);
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = new RestTemplate()
                .exchange(ssoValues.getProfileUrl(), HttpMethod.GET, request, Map.class);
        return new User(response.getBody(), ssoValues);
    }


    private static class User {

        private String id;

        private String firstName;

        private String lastName;

        User(Map values, SSOProperties.SSOValues keys) {
            id = values.get(keys.getId()).toString();
            firstName = values.get(keys.getFirstname()).toString();
            lastName = values.get(keys.getLastname()).toString();
        }
    }
}