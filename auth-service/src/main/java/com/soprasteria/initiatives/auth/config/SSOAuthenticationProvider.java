package com.soprasteria.initiatives.auth.config;

import com.soprasteria.initiatives.auth.config.properties.SSOProperties;
import com.soprasteria.initiatives.auth.service.UserService;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.soprasteria.initiatives.auth.config.properties.SSOProperties.SSOValues;

@Configuration
public class SSOAuthenticationProvider implements AuthenticationProvider {

    private SSOProperties ssoProperties;

    private UserService userService;

    public SSOAuthenticationProvider(SSOProperties ssoProperties, UserService userService) {
        this.ssoProperties = ssoProperties;
        this.userService = userService;
    }


    @Override
    public Authentication authenticate(Authentication authentication) {
        Map details = (Map) authentication.getDetails();
        SSOProvider ssoProvider = SSOProvider.fromString((String) details.get("ssoProvider"));
        SSOValues ssoValues = ssoProperties.getProviders().get(ssoProvider);
        UserSSO userSSO = callSSOProvider(authentication.getPrincipal().toString(), ssoValues);
        return buildAuthentication(userSSO, ssoProvider);
    }

    private UserSSO callSSOProvider(String accessToken, SSOValues ssoValues) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, OAuth2AccessToken.BEARER_TYPE + " " + accessToken);
        HttpEntity request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = new RestTemplate()
                .exchange(ssoValues.getProfileUrl(), HttpMethod.GET, request, Map.class);
        return new UserSSO(response.getBody(), ssoValues);
    }

    private Authentication buildAuthentication(UserSSO userSSO, SSOProvider ssoProvider) {
        String username = ssoProvider.toString() + "#" + userSSO.id;
        List<GrantedAuthority> authorities = findGrantedAuthorities(username);
        AuthenticatedUser user = new AuthenticatedUser("", username, userSSO.firstName, userSSO.lastName, authorities);
        return new UsernamePasswordAuthenticationToken(user, "", authorities);
    }

    private List<GrantedAuthority> findGrantedAuthorities(String username) {
        return userService.findByAuthoritiesByUsername(username).stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }


    private static class UserSSO {

        private String id;

        private String firstName;

        private String lastName;

        UserSSO(Map values, SSOValues keys) {
            id = values.get(keys.getId()).toString();
            firstName = values.get(keys.getFirstName()).toString();
            lastName = values.get(keys.getLastName()).toString();
        }
    }
}