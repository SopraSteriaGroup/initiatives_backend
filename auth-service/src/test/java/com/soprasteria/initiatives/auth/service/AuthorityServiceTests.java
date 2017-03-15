package com.soprasteria.initiatives.auth.service;

import com.soprasteria.initiatives.auth.dao.AuthorityDaoTests;
import com.soprasteria.initiatives.auth.dao.UserAuthorityDaoTests;
import com.soprasteria.initiatives.auth.domain.Authority;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Business test for {@link Authority} entity
 *
 * @author jntakpe
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AuthorityServiceTests extends AbstractDBServiceTests {

    private static final String TABLE_NAME = "authority";

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private AuthorityDaoTests authorityDaoTests;

    @Autowired
    private UserAuthorityDaoTests userAuthorityDaoTests;

    @Test
    public void findAll_shouldFind() {
        assertThat(authorityService.findAll()).isNotEmpty().hasSize(nbEntries);
    }

    @Test
    public void findById_shouldFind() {
        assertThat(authorityService.findById(authorityDaoTests.findAny().getId())).isNotNull();
    }

    @Test(expected = EntityNotFoundException.class)
    public void findById_shouldFailCuzUnknownId() {
        authorityService.findById(9999L);
        fail("should have failed at this point");
    }

    @Test
    public void findDefaultOrCreate_shouldFindDefault() {
        if (authorityDaoTests.countByName(Authority.DEFAULT_AUTHORITY) != 1) {
            jdbcTemplate.execute("INSERT INTO authority (name) VALUES (" + Authority.DEFAULT_AUTHORITY + ")");
        }
        assertThat(authorityDaoTests.countByName(Authority.DEFAULT_AUTHORITY)).isEqualTo(1L);
        assertThat(authorityService.findDefaultOrCreate()).isNotNull().extracting(Authority::getId).isNotNull();
    }

    @Test
    public void findDefaultOrCreate_shouldCreateDefaultThenFind() {
        if (authorityDaoTests.countByName(Authority.DEFAULT_AUTHORITY) == 1) {
            jdbcTemplate.execute("DELETE FROM authority WHERE name = '" + Authority.DEFAULT_AUTHORITY + "'");
        }
        assertThat(authorityDaoTests.countByName(Authority.DEFAULT_AUTHORITY)).isZero();
        assertThat(authorityService.findDefaultOrCreate()).isNotNull().extracting(Authority::getId).isNotNull();
    }

    @Test
    public void create_shouldCreate() {
        Authority authority = authorityService.create(new Authority("ROLE_TEST"));
        assertThat(authority).isNotNull().extracting(Authority::getId).isNotNull();
        assertThat(countRowsInCurrentTable()).isEqualTo(nbEntries + 1);
    }

    @Test(expected = ValidationException.class)
    public void create_shouldFailCuzNameTaken() {
        authorityService.create(new Authority(authorityDaoTests.findAny().getName()));
        fail("should have failed at this point");
    }

    @Test
    public void edit_shouldChangeName() {
        Authority initAuth = authorityDaoTests.findAny();
        String initName = initAuth.getName();
        String roleEdited = "ROLE_EDITED";
        initAuth.setName(roleEdited);
        Authority edited = authorityService.edit(initAuth.getId(), initAuth);
        assertThat(edited.getName()).isNotEqualTo(initName);
        assertThat(authorityDaoTests.countByName(initName)).isZero();
        assertThat(authorityDaoTests.countByName(edited.getName())).isEqualTo(1L);
        assertThat(countRowsInCurrentTable()).isEqualTo(nbEntries);
    }

    @Test(expected = ValidationException.class)
    public void edit_shouldFailCuzNameTaken() {
        Authority authority = authorityDaoTests.findAny();
        authority.setName(authorityDaoTests.findAnyButThis(authority).getName());
        authorityService.edit(authority.getId(), authority);
        fail("should have failed at this point");
    }

    @Test
    public void delete_shouldRemoveOne() {
        Authority authority = authorityDaoTests.findAny();
        authorityService.delete(authority.getId());
        assertThat(countRowsInCurrentTable()).isEqualTo(nbEntries - 1);
        assertThat(authorityDaoTests.exist(authority.getId())).isFalse();
    }

    @Test(expected = EntityNotFoundException.class)
    public void delete_shouldFailCuzUnknownId() {
        authorityService.delete(9999L);
    }

    @Test
    public void delete_shouldRemoveUserAuthoritiesLink() {
        Authority authority = authorityDaoTests.findAny();
        authorityService.delete(authority.getId());
        assertThat(userAuthorityDaoTests.countAuthorityIdOccurences(authority.getId())).isZero();
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
