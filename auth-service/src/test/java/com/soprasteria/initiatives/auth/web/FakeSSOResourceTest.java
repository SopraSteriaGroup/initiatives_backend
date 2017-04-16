package com.soprasteria.initiatives.auth.web;

import com.soprasteria.initiatives.commons.api.SecurityConstants;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Resource fake SSO endpoint test
 *
 * @author cegiraud
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class FakeSSOResourceTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc realMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.realMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void me_shouldAuthorized() throws Exception {
        JSONObject user = new JSONObject();
        user.put("id", "1");
        user.put("firstName", "firstName");
        user.put("lastName", "lastName");
        String accessToken = Base64.getUrlEncoder().withoutPadding().encodeToString(user.toString().getBytes());
        realMvc.perform(get(ApiConstants.FAKE_SSO)
                .contentType(MediaType.APPLICATION_JSON)
                .header("authorization", SecurityConstants.BEARER_PREFIX + accessToken))
                .andExpect(AssertionConstants.STATUS_OK)
                .andExpect(AssertionConstants.OBJECT_EXISTS)
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists());
    }

    @Test
    public void createAccessToken_shouldCreate() throws Exception {
        JSONObject user = new JSONObject();
        user.put("id", "idsso");
        user.put("firstName", "firstName");
        user.put("lastName", "lastName");
        String s = Base64.getUrlEncoder().withoutPadding().encodeToString(user.toString().getBytes());
        realMvc.perform(get(ApiConstants.TOKENS)
                .contentType(MediaType.APPLICATION_JSON)
                .param("idsso", "idsso")
                .param("firstName", "firstName")
                .param("lastName", "lastName"))
                .andExpect(AssertionConstants.STATUS_OK)
                .andExpect(AssertionConstants.OBJECT_EXISTS)
                .andExpect(content().bytes(s.getBytes()));
    }
}