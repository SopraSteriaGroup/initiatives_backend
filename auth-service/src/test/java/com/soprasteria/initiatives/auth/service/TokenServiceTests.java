package com.soprasteria.initiatives.auth.service;

import com.soprasteria.initiatives.auth.config.CustomUser;
import com.soprasteria.initiatives.auth.web.TokenResourceTests;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Business tests for token retrieval
 *
 * @author jntakpe
 */
@DirtiesContext
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TokenServiceTests {

    @Autowired
    private TokenService tokenService;

    @Mock
    private UserDetailsService userDetailsService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }


    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void authorize_shouldFailCuzUnknownUser() {
        when(userDetailsService.loadUserByUsername("accessToken")).thenReturn(null);

        ResponseEntity<OAuth2AccessToken> responseEntity =
                tokenService.authorize("accessToken", TokenResourceTests.OAUTH2_TOKEN_URL);
        assertThat(responseEntity.getStatusCode().is4xxClientError()).isTrue();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(responseEntity.getBody()).isNull();
    }

}
