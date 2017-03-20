package com.soprasteria.initiatives.auth.web;

import com.soprasteria.initiatives.auth.service.TokenService;
import com.soprasteria.initiatives.auth.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Token retrieval API testing
 *
 * @author jntakpe
 */
@DirtiesContext
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TokenResourceTests {

    public static final String OAUTH2_TOKEN_URL = "http://localhost/api/tokens";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    private TokenService mockTokenService;

    @Mock(name = "userServiceMock")
    private UserService userService;

    private MockMvc realMvc;

    private MockMvc mockMvc;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.realMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.mockMvc = MockMvcBuilders.standaloneSetup(new TokenResource(mockTokenService)).build();
    }


    @Test
    public void authorize_shouldUnauthorizedCuzInvalidToken() throws Exception {
        when(mockTokenService.authorize("invalidAccessToken", OAUTH2_TOKEN_URL))
                .thenReturn(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
        mockMvc.perform(post(ApiConstants.TOKENS)
                .header(HttpHeaders.AUTHORIZATION, OAuth2AccessToken.BEARER_TYPE + " invalidAccessToken")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_UNAUTHORIZED);
    }
}
