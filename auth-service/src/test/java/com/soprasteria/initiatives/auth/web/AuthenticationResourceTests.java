package com.soprasteria.initiatives.auth.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soprasteria.initiatives.auth.dao.UserDaoTests;
import com.soprasteria.initiatives.auth.domain.Authority;
import com.soprasteria.initiatives.auth.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.OAuth2ClientProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests associés aux endpoints OAuth2 permettant de générer un token
 *
 * @author jntakpe
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AuthenticationResourceTests {

    private static final String USER_NAME_KEY = "user_name";

    private static final String AUTHORITIES = "authorities";

    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";

    private static final String ACCESS_TOKEN_KEY = "access_token";

    private static final String REFRESH_TOKEN_KEY = "refresh_token";

    private MockMvc realMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OAuth2ClientProperties oAuth2ClientProperties;

    @Autowired
    private UserDaoTests userDaoTests;

    private String authorizationHeader;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.realMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).apply(springSecurity()).build();
        String clientSecret = new StringJoiner(":")
                .add(oAuth2ClientProperties.getClientId())
                .add(oAuth2ClientProperties.getClientSecret())
                .toString();
        this.authorizationHeader = "Basic " + Base64.getEncoder().encodeToString(clientSecret.getBytes());
    }

    @Test
    public void token_shouldReturnAcccessToken() throws Exception {
        User user = userDaoTests.findAny();
        realMvc.perform(buildTokenRequest(user.getUsername(), user.getPassword()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$." + ACCESS_TOKEN_KEY).exists())
                .andExpect(jsonPath("$." + REFRESH_TOKEN_KEY).exists())
                .andExpect(jsonPath("$.expires_in").exists())
                .andExpect(jsonPath("$.token_type").exists());
    }

    @Test
    public void token_should400CuzWrongPwd() throws Exception {
        realMvc.perform(buildTokenRequest(userDaoTests.findAny().getUsername(), "wrongpwd").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void token_should400CuzWrongUsername() throws Exception {
        realMvc.perform(buildTokenRequest("wrongusername", userDaoTests.findAny().getPassword()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void refreshAccessToken_shouldGetNewAccessToken() throws Exception {
        User user = userDaoTests.findAny();
        Map<String, String> tokenMap = getTokenMap(user.getUsername(), user.getPassword());
        String refreshToken = tokenMap.get(REFRESH_TOKEN_KEY);
        String oldAccessToken = tokenMap.get(ACCESS_TOKEN_KEY);
        ResultActions resultActions = realMvc.perform(post("/oauth/token")
                .header(AUTHORIZATION_HEADER_KEY, authorizationHeader)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("grant_type", "refresh_token")
                .param(REFRESH_TOKEN_KEY, refreshToken));
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$." + ACCESS_TOKEN_KEY).exists());
        Map<String, String> refreshedTokenMap = tokenMapFromResultActions(resultActions);
        assertThat(refreshedTokenMap.get(ACCESS_TOKEN_KEY)).isNotEqualTo(oldAccessToken);
    }

    @Test
    public void jwt_shouldContainsFields() throws Exception {
        User user = userDaoTests.findAny();
        String accessToken = getTokenMap(user.getUsername(), user.getPassword()).get(ACCESS_TOKEN_KEY);
        String claims = JwtHelper.decode(accessToken).getClaims();
        Map<String, Object> map = objectMapper.readValue(claims, new TypeReference<Map<String, Object>>() {
        });
        assertThat(map.containsKey(USER_NAME_KEY)).isTrue();
        assertThat(map.containsKey("scope")).isTrue();
        assertThat(map.containsKey("exp")).isTrue();
        assertThat(map.containsKey(AUTHORITIES)).isTrue();
        assertThat(map.containsKey("jti")).isTrue();
        assertThat(map.containsKey("client_id")).isTrue();
    }

    @Test
    public void jwt_shouldContainSpecificUsername() throws Exception {
        User user = userDaoTests.findAny();
        String accessToken = getTokenMap(user.getUsername(), user.getPassword()).get(ACCESS_TOKEN_KEY);
        String claims = JwtHelper.decode(accessToken).getClaims();
        Map<String, Object> map = objectMapper.readValue(claims, new TypeReference<Map<String, Object>>() {
        });
        assertThat(map.containsKey(USER_NAME_KEY)).isTrue();
        String username = (String) map.get(USER_NAME_KEY);
        assertThat(user.getUsername()).isEqualTo(username);
    }

    @Test
    public void jwt_shouldContainSpecificAuthorities() throws Exception {
        User user = userDaoTests.findAnyWithAuthorities();
        String accessToken = getTokenMap(user.getUsername(), user.getPassword()).get(ACCESS_TOKEN_KEY);
        String claims = JwtHelper.decode(accessToken).getClaims();
        Map<String, Object> map = objectMapper.readValue(claims, new TypeReference<Map<String, Object>>() {
        });
        assertThat(map.containsKey(AUTHORITIES)).isTrue();
        List<String> authorities = (ArrayList<String>) map.get(AUTHORITIES);
        assertThat(authorities).isNotEmpty();
        List<String> initAuths = user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toList());
        assertThat(authorities).hasSameSizeAs(initAuths).containsAll(initAuths);
    }

    private Map<String, String> getTokenMap(String username, String password) throws Exception {
        ResultActions resultActions = realMvc.perform(buildTokenRequest(username, password).accept(MediaType.APPLICATION_JSON));
        return tokenMapFromResultActions(resultActions);
    }

    private Map<String, String> tokenMapFromResultActions(ResultActions resultActions) throws java.io.IOException {
        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        TypeReference<Map<String, String>> mapTypeRef = new TypeReference<Map<String, String>>() {
        };
        return objectMapper.readValue(response.getContentAsByteArray(), mapTypeRef);
    }

    private MockHttpServletRequestBuilder buildTokenRequest(String username, String password) {
        return post("/oauth/token")
                .header(AUTHORIZATION_HEADER_KEY, authorizationHeader)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", username)
                .param("password", password)
                .param("scope", "openid")
                .param("client_id", oAuth2ClientProperties.getClientId())
                .param("secret", oAuth2ClientProperties.getClientSecret())
                .param("grant_type", "password");
    }


}
