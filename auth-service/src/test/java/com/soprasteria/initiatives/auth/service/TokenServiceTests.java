package com.soprasteria.initiatives.auth.service;

import com.soprasteria.initiatives.auth.dao.UserDaoTests;
import com.soprasteria.initiatives.auth.domain.User;
import com.soprasteria.initiatives.auth.web.TokenResourceTests;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Autowired
    private UserDaoTests userDaoTests;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void authorize_shouldAuthorizeUser() {
        ResponseEntity<OAuth2AccessToken> responseEntity =
                tokenService.authorize(userDaoTests.findAny(), TokenResourceTests.OAUTH2_TOKEN_URL);
        assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(responseEntity.getBody()).isNotNull().extracting(OAuth2AccessToken::getValue).isNotEmpty();
    }

    @Test
    public void authorize_shouldFailCuzUnknownUser() {
        ResponseEntity<OAuth2AccessToken> responseEntity =
                tokenService.authorize(new User("toto", "titipwd"), TokenResourceTests.OAUTH2_TOKEN_URL);
        assertThat(responseEntity.getStatusCode().is4xxClientError()).isTrue();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void authorize_shouldFailCuzUnknownPwd() {
        User user = new User(userDaoTests.findAny().getUsername(), "titipwd");
        ResponseEntity<OAuth2AccessToken> responseEntity = tokenService.authorize(user, TokenResourceTests.OAUTH2_TOKEN_URL);
        assertThat(responseEntity.getStatusCode().is4xxClientError()).isTrue();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(responseEntity.getBody()).isNull();
    }
}
