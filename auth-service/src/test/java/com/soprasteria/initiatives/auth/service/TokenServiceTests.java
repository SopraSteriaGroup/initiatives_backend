package com.soprasteria.initiatives.auth.service;

import com.soprasteria.initiatives.auth.utils.SSOProvider;
import com.soprasteria.initiatives.auth.web.FakeSSOResource;
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
    private FakeSSOResource fakeSSOResource;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void authorize_shouldAuthorizeUser() throws Exception {
        String accessToken = fakeSSOResource.createAccessToken("1", "test", "test");
        ResponseEntity<OAuth2AccessToken> responseEntity =
                tokenService.authorize(accessToken, SSOProvider.FAKE_SSO, TokenResourceTests.OAUTH2_TOKEN_URL);
        assertThat(responseEntity.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(responseEntity.getBody()).isNotNull().extracting(OAuth2AccessToken::getValue).isNotEmpty();
    }

    @Test
    public void authorize_shouldFailCuzUnknownUser() {
        ResponseEntity<OAuth2AccessToken> responseEntity =
                tokenService.authorize("invalidAccessToken", SSOProvider.FAKE_SSO, TokenResourceTests.OAUTH2_TOKEN_URL);
        assertThat(responseEntity.getStatusCode().is4xxClientError()).isTrue();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(responseEntity.getBody()).isNull();
    }

}
