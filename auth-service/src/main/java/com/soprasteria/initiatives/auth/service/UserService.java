package com.soprasteria.initiatives.auth.service;

import com.soprasteria.initiatives.auth.config.CustomUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author jntakpe
 * @author cegiraud
 */
@Service
public class UserService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private static final String LINKEDIN_PROFILE = "https://api.linkedin.com/v1/people/~?format=json";

    @Override
    public UserDetails loadUserByUsername(String authorization) {
        LOGGER.debug("Trying to connect to linkedin api.");
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, OAuth2AccessToken.BEARER_TYPE + " " + authorization);
        HttpEntity request = new HttpEntity<>(headers);

        ResponseEntity<User> userResponse = new RestTemplate().exchange(LINKEDIN_PROFILE, HttpMethod.GET, request, User.class);
        User user = userResponse.getBody();

        //TODO appel MS pour reccupérer les roles.
        List<GrantedAuthority> roles = AuthorityUtils.createAuthorityList();
        return new CustomUser(user.getId(), "", user.getFirstName(), user.getLastName(), roles);
    }


    private static class User {

        private String id;

        private String firstName;

        private String lastName;

        String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }
}