package com.soprasteria.initiatives.auth.web;

import com.soprasteria.initiatives.auth.dao.AuthorityDaoTests;
import com.soprasteria.initiatives.auth.domain.Authority;
import com.soprasteria.initiatives.auth.service.AuthorityService;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * {@link Authority} REST API testing
 *
 * @author jntakpe
 */
public class AuthorityResourceTests extends AbstractResourceTests {

    @Autowired
    private AuthorityDaoTests authorityDaoTests;

    @Mock
    private AuthorityService mockAuthorityService;

    @Test
    public void findAll_shouldFind() throws Exception {
        realMvc.perform(get(ApiConstants.AUTHORITIES).accept(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_OK)
                .andExpect(AssertionConstants.CONTENT_TYPE_JSON)
                .andExpect(jsonPath("$.[*]").isNotEmpty())
                .andExpect(jsonPath("$.[*].name").isNotEmpty());
    }

    @Test
    public void findAll_shouldBeEmpty() throws Exception {
        when(mockAuthorityService.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get(ApiConstants.AUTHORITIES).accept(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_OK)
                .andExpect(AssertionConstants.CONTENT_TYPE_JSON)
                .andExpect(jsonPath("$.[*]").isEmpty())
                .andExpect(jsonPath("$.[*].name").isEmpty());
    }

    @Test
    public void find_shouldFind() throws Exception {
        Authority authority = authorityDaoTests.findAny();
        realMvc.perform(get(ApiConstants.AUTHORITIES + "/{authorityId}", authority.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_OK)
                .andExpect(AssertionConstants.CONTENT_TYPE_JSON)
                .andExpect(AssertionConstants.OBJECT_EXISTS)
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value(authority.getName()));
    }

    @Test
    public void find_shouldNotFind() throws Exception {
        realMvc.perform(get(ApiConstants.AUTHORITIES + "/{authorityId}", 9999L).accept(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_NOT_FOUND);
    }

    @Test
    public void create_shouldCreate() throws Exception {
        String name = "ROLE_TEST_RES";
        Authority authority = new Authority(name);
        realMvc.perform(post(ApiConstants.AUTHORITIES).content(objectMapper.writeValueAsBytes(authority))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_CREATED)
                .andExpect(AssertionConstants.CONTENT_TYPE_JSON)
                .andExpect(AssertionConstants.OBJECT_EXISTS)
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    public void create_shouldFailCuzMissingName() throws Exception {
        Authority authority = new Authority();
        realMvc.perform(post(ApiConstants.AUTHORITIES).content(objectMapper.writeValueAsBytes(authority))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_BAD_REQUEST);
    }

    @Test
    public void create_shouldFailCuzNameTaken() throws Exception {
        Authority authority = new Authority(authorityDaoTests.findAny().getName());
        realMvc.perform(post(ApiConstants.AUTHORITIES).content(objectMapper.writeValueAsBytes(authority))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_BAD_REQUEST);
    }

    @Test
    public void update_shouldEdit() throws Exception {
        Authority authority = authorityDaoTests.findAny();
        String editedName = "ROLE_EDITED";
        authority.setName(editedName);
        realMvc.perform(put(ApiConstants.AUTHORITIES + "/{authorityId}", authority.getId())
                .content(objectMapper.writeValueAsBytes(authority))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_OK)
                .andExpect(AssertionConstants.CONTENT_TYPE_JSON)
                .andExpect(AssertionConstants.OBJECT_EXISTS)
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value(editedName));
    }

    @Test
    public void update_shouldFailCuzMissingName() throws Exception {
        Authority authority = authorityDaoTests.findAny();
        authority.setName(null);
        realMvc.perform(put(ApiConstants.AUTHORITIES + "/{authorityId}", authority.getId())
                .content(objectMapper.writeValueAsBytes(authority))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_BAD_REQUEST);
    }

    @Test
    public void update_shouldFailCuzNameTaken() throws Exception {
        Authority authority = authorityDaoTests.findAny();
        authority.setId(authorityDaoTests.findAnyButThis(authority).getId());
        realMvc.perform(put(ApiConstants.AUTHORITIES + "/{authorityId}", authority.getId())
                .content(objectMapper.writeValueAsBytes(authority))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_BAD_REQUEST);
    }

    @Test
    public void delete_shouldDelete() throws Exception {
        realMvc.perform(MockMvcRequestBuilders.delete(ApiConstants.AUTHORITIES + "/{authorityId}", authorityDaoTests.findAny().getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_NO_CONTENT)
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void delete_shouldFailCuzIdDoesntExist() throws Exception {
        realMvc.perform(delete(ApiConstants.AUTHORITIES + "/{authorityId}", 999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(AssertionConstants.STATUS_NOT_FOUND);
    }

    @Override
    public Object getMockResource() {
        return new AuthorityResource(mockAuthorityService);
    }
}
