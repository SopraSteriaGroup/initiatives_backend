package com.soprasteria.initiatives.auth.web;

import com.soprasteria.initiatives.auth.dao.UserAuthorityDaoTests;
import com.soprasteria.initiatives.auth.dao.UserDaoTests;
import com.soprasteria.initiatives.auth.dao.UserIdAuthorityId;
import com.soprasteria.initiatives.auth.domain.User;
import com.soprasteria.initiatives.auth.service.UserService;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * {@link User} REST API testing
 *
 * @author jntakpe
 */
public class UserResourceTests extends AbstractResourceTests {

    @Autowired
    private UserDaoTests userDaoTests;

    @Autowired
    private UserAuthorityDaoTests userAuthorityDaoTests;

    @Mock
    private UserService mockUserService;

    @Test
    public void findAll_shouldFind() throws Exception {
        realMvc.perform(get(ApiConstants.USERS).accept(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_OK)
                .andExpect(AssertionConstants.CONTENT_TYPE_JSON)
                .andExpect(jsonPath("$.[*]").isNotEmpty())
                .andExpect(jsonPath("$.[*].username").isNotEmpty());
    }

    @Test
    public void findAll_shouldBeEmpty() throws Exception {
        when(mockUserService.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get(ApiConstants.USERS).accept(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_OK)
                .andExpect(AssertionConstants.CONTENT_TYPE_JSON)
                .andExpect(jsonPath("$.[*]").isEmpty())
                .andExpect(jsonPath("$.[*].username").isEmpty());
    }

    @Test
    public void find_shouldFind() throws Exception {
        User user = userDaoTests.findAny();
        realMvc.perform(get(ApiConstants.USERS + "/{userId}", user.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_OK)
                .andExpect(AssertionConstants.CONTENT_TYPE_JSON)
                .andExpect(AssertionConstants.OBJECT_EXISTS)
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.password").value(user.getPassword()));
    }

    @Test
    public void find_shouldNotFind() throws Exception {
        realMvc.perform(get(ApiConstants.USERS + "/{userId}", 9999L).accept(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_NOT_FOUND);
    }

    @Test
    public void create_shouldCreate() throws Exception {
        String username = "restestusername";
        String password = "restestpwd";
        User user = new User(username, password);
        realMvc.perform(post(ApiConstants.USERS).content(objectMapper.writeValueAsBytes(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_CREATED)
                .andExpect(AssertionConstants.CONTENT_TYPE_JSON)
                .andExpect(AssertionConstants.OBJECT_EXISTS)
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.password").value(password));
    }

    @Test
    public void create_shouldFailCuzMissingUsername() throws Exception {
        User user = new User(null, "password");
        realMvc.perform(post(ApiConstants.USERS).content(objectMapper.writeValueAsBytes(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_BAD_REQUEST);
    }

    @Test
    public void create_shouldFailCuzMissingPassword() throws Exception {
        User user = new User("username", null);
        realMvc.perform(post(ApiConstants.USERS).content(objectMapper.writeValueAsBytes(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_BAD_REQUEST);
    }

    @Test
    public void create_shouldFailCuzUsernameTaken() throws Exception {
        User user = new User(userDaoTests.findAny().getUsername(), "password");
        realMvc.perform(post(ApiConstants.USERS).content(objectMapper.writeValueAsBytes(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_BAD_REQUEST);
    }

    @Test
    public void update_shouldEdit() throws Exception {
        User user = userDaoTests.findAny();
        String editedUsername = "editedusername";
        user.setUsername(editedUsername);
        realMvc.perform(put(ApiConstants.USERS + "/{userId}", user.getId())
                .content(objectMapper.writeValueAsBytes(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_OK)
                .andExpect(AssertionConstants.CONTENT_TYPE_JSON)
                .andExpect(AssertionConstants.OBJECT_EXISTS)
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.username").value(editedUsername))
                .andExpect(jsonPath("$.password").value(user.getPassword()));
    }

    @Test
    public void update_shouldFailCuzMissingUsername() throws Exception {
        User user = userDaoTests.findAny();
        user.setUsername(null);
        realMvc.perform(put(ApiConstants.USERS + "/{userId}", user.getId()).content(objectMapper.writeValueAsBytes(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_BAD_REQUEST);
    }

    @Test
    public void update_shouldFailCuzMissingPassword() throws Exception {
        User user = userDaoTests.findAny();
        user.setPassword(null);
        realMvc.perform(put(ApiConstants.USERS + "/{userId}", user.getId()).content(objectMapper.writeValueAsBytes(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_BAD_REQUEST);
    }

    @Test
    public void update_shouldFailCuzUsernameTaken() throws Exception {
        User user = userDaoTests.findAny();
        user.setId(userDaoTests.findAnyButThis(user).getId());
        realMvc.perform(put(ApiConstants.USERS + "/{userId}", user.getId()).content(objectMapper.writeValueAsBytes(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_BAD_REQUEST);
    }

    @Test
    public void delete_shouldDelete() throws Exception {
        realMvc.perform(delete(ApiConstants.USERS + "/{userId}", userDaoTests.findAny().getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_NO_CONTENT);
    }

    @Test
    public void delete_shouldFailCuzIdDoesntExist() throws Exception {
        realMvc.perform(delete(ApiConstants.USERS + "/{userId}", 999L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_NOT_FOUND);
    }

    @Test
    public void findAuthorities_shouldFind() throws Exception {
        realMvc.perform(get(ApiConstants.USERS_AUTHORITIES, userAuthorityDaoTests.findAny().getUserId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_OK)
                .andExpect(AssertionConstants.CONTENT_TYPE_JSON)
                .andExpect(jsonPath("$.[*]").isNotEmpty())
                .andExpect(jsonPath("$.[*].name").isNotEmpty());
    }

    @Test
    public void addAuthority_shoudAdd() throws Exception {
        UserIdAuthorityId ua = userAuthorityDaoTests.findAuthorityToBeAdded();
        realMvc.perform(put(ApiConstants.USERS_AUTHORITIES + "/{authorityId}", ua.getUserId(), ua.getAuthorityId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_OK)
                .andExpect(AssertionConstants.CONTENT_TYPE_JSON)
                .andExpect(AssertionConstants.OBJECT_EXISTS)
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.username").exists());
    }

    @Test
    public void addAuthority_shoudFailCuzAlreadyAdded() throws Exception {
        UserIdAuthorityId ua = userAuthorityDaoTests.findAny();
        realMvc.perform(put(ApiConstants.USERS_AUTHORITIES + "/{authorityId}", ua.getUserId(), ua.getAuthorityId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_BAD_REQUEST);
    }

    @Test
    public void removeAuthority_shoudAdd() throws Exception {
        UserIdAuthorityId ua = userAuthorityDaoTests.findAny();
        realMvc.perform(delete(ApiConstants.USERS_AUTHORITIES + "/{authorityId}", ua.getUserId(), ua.getAuthorityId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_OK)
                .andExpect(AssertionConstants.CONTENT_TYPE_JSON)
                .andExpect(AssertionConstants.OBJECT_EXISTS)
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.username").exists());
    }

    @Test
    public void removeAuthority_shoudFailCuzDoesntExist() throws Exception {
        UserIdAuthorityId ua = userAuthorityDaoTests.findAuthorityToBeAdded();
        realMvc.perform(delete(ApiConstants.USERS_AUTHORITIES + "/{authorityId}", ua.getUserId(), 9999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_NOT_FOUND);
    }

    @Test
    public void removeAuthority_shoudFailCuzDoesntExistForThatUser() throws Exception {
        UserIdAuthorityId ua = userAuthorityDaoTests.findAuthorityToBeAdded();
        realMvc.perform(delete(ApiConstants.USERS_AUTHORITIES + "/{authorityId}", ua.getUserId(), ua.getAuthorityId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_BAD_REQUEST);
    }

    @Override
    public Object getMockResource() {
        return new UserResource(mockUserService);
    }

}
