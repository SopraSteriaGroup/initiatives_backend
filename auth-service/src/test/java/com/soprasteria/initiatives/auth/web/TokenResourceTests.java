package com.soprasteria.initiatives.auth.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soprasteria.initiatives.auth.dao.UserDaoTests;
import com.soprasteria.initiatives.auth.domain.User;
import com.soprasteria.initiatives.auth.service.TokenService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserDaoTests userDaoTests;

    @Mock
    private TokenService mockTokenService;

    private MockMvc realMvc;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.realMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.mockMvc = MockMvcBuilders.standaloneSetup(new TokenResource(mockTokenService)).build();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void authorize_shouldRetrieveToken() throws Exception {
        realMvc.perform(post(ApiConstants.TOKENS).content(objectMapper.writeValueAsBytes(userDaoTests.findAny()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_OK)
                .andExpect(AssertionConstants.CONTENT_TYPE_JSON)
                .andExpect(AssertionConstants.OBJECT_EXISTS)
                .andExpect(jsonPath("$.access_token").exists())
                .andExpect(jsonPath("$.scope").exists())
                .andExpect(jsonPath("$.expires_in").exists())
                .andExpect(jsonPath("$.refresh_token").exists());
    }

    @Test
    public void authorize_shouldFailCuzMissingUsername() throws Exception {
        User user = new User(null, "pwd");
        realMvc.perform(post(ApiConstants.TOKENS).content(objectMapper.writeValueAsBytes(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_BAD_REQUEST);
    }

    @Test
    public void authorize_shouldFailCuzMissingPwd() throws Exception {
        User user = new User(userDaoTests.findAny().getUsername(), null);
        realMvc.perform(post(ApiConstants.TOKENS).content(objectMapper.writeValueAsBytes(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_BAD_REQUEST);
    }

    @Test
    public void authorize_shouldUnauthorizedCuzInvalidUser() throws Exception {
        User user = new User("unknownuser", "pwd");
        when(mockTokenService.authorize(user, OAUTH2_TOKEN_URL)).thenReturn(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
        mockMvc.perform(post(ApiConstants.TOKENS).content(objectMapper.writeValueAsBytes(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_UNAUTHORIZED);
    }
}
