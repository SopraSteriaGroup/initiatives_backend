package com.soprasteria.initiatives.auth.service;

import com.soprasteria.initiatives.auth.dao.UserAuthorityDaoTests;
import com.soprasteria.initiatives.auth.dao.UserDaoTests;
import com.soprasteria.initiatives.auth.dao.UserIdAuthorityId;
import com.soprasteria.initiatives.auth.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Business test for {@link User} entity
 *
 * @author jntakpe
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTests extends AbstractDBServiceTests {

    private static final String TABLE_NAME = "user";

    @Autowired
    private UserService userService;

    @Autowired
    private UserDaoTests userDaoTests;

    @Autowired
    private UserAuthorityDaoTests userAuthorityDaoTests;

    @Test
    public void findAll_shouldFind() {
        assertThat(userService.findAll()).isNotEmpty().hasSize(nbEntries);
    }

    @Test
    public void findById_shouldFind() {
        assertThat(userService.findById(userDaoTests.findAny().getId())).isNotNull();
    }

    @Test(expected = EntityNotFoundException.class)
    public void findById_shouldFailCuzUnknownId() {
        userService.findById(9999L);
        fail("should have failed at this point");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findByUsername_shouldNotFindCuzUsernameDoesntExist() {
        userService.findByUsername("unknown");
        fail("should have failed at this point");
    }

    @Test
    public void findByUsername_shouldFindWithExactMatch() {
        assertThat(userService.findByUsername(userDaoTests.findAny().getUsername())).isNotNull();
    }

    @Test
    public void findByUsername_shouldFindIgnoringCase() {
        assertThat(userService.findByUsername(userDaoTests.findAny().getUsername().toUpperCase())).isNotNull();
    }

    @Test
    public void create_shouldCreate() {
        User createdUser = userService.create(new User("newuser", "newpwd"));
        assertThat(createdUser).isNotNull().extracting(User::getId).isNotNull();
        assertThat(countRowsInCurrentTable()).isEqualTo(nbEntries + 1);
    }

    @Test(expected = ValidationException.class)
    public void create_shouldFailCuzUsernameTaken() {
        userService.create(new User(userDaoTests.findAny().getUsername(), "newpwd"));
        fail("should have failed at this point");
    }

    @Test
    public void create_shouldAddDefaultAuth() {
        User user = userService.create(new User("withAuth", "somepwd"));
        assertThat(user).isNotNull().extracting(User::getId).isNotNull();
        assertThat(countRowsInCurrentTable()).isEqualTo(nbEntries + 1);
        assertThat(userAuthorityDaoTests.countUserIdOccurences(user.getId())).isEqualTo(1L);
    }

    @Test
    public void edit_shouldChangeUsername() {
        User toEdit = userDaoTests.findAny();
        String initUsername = toEdit.getUsername();
        String editedUsername = "editeduser";
        toEdit.setUsername(editedUsername);
        User edited = userService.edit(toEdit.getId(), toEdit);
        assertThat(edited.getUsername()).isNotEqualTo(initUsername).isEqualTo(editedUsername);
        assertThat(userDaoTests.countByUsername(initUsername)).isZero();
        assertThat(userDaoTests.countByUsername(editedUsername)).isEqualTo(1L);
        assertThat(countRowsInCurrentTable()).isEqualTo(nbEntries);
    }

    @Test(expected = ValidationException.class)
    public void edit_shouldFailCuzUsernameTaken() {
        User toEdit = userDaoTests.findAny();
        toEdit.setUsername(userDaoTests.findAnyButThis(toEdit).getUsername());
        userService.edit(toEdit.getId(), toEdit);
        fail("should have failed at this point");
    }

    @Test
    public void edit_shouldChangePassword() {
        User toEdit = userDaoTests.findAny();
        String initPwd = toEdit.getPassword();
        Long initCount = userDaoTests.countByPassword(initPwd);
        String editedPwd = "editedpwd";
        toEdit.setPassword(editedPwd);
        userService.edit(toEdit.getId(), toEdit);
        assertThat(userDaoTests.countByPassword(initPwd)).isEqualTo(initCount - 1);
        assertThat(userDaoTests.countByPassword(editedPwd)).isEqualTo(1L);
        assertThat(countRowsInCurrentTable()).isEqualTo(nbEntries);
    }

    @Test
    public void delete_shouldRemoveOne() {
        User user = userDaoTests.findAny();
        userService.delete(user.getId());
        assertThat(countRowsInCurrentTable()).isEqualTo(nbEntries - 1);
        assertThat(userDaoTests.exist(user.getId())).isFalse();
    }

    @Test(expected = EntityNotFoundException.class)
    public void delete_shouldFailCuzUnknownId() {
        userService.delete(9999L);
    }

    @Test
    public void delete_shouldRemoveUserAuthoritiesLink() {
        User user = userDaoTests.findAny();
        userService.delete(user.getId());
        assertThat(userAuthorityDaoTests.countUserIdOccurences(user.getId())).isZero();
    }

    @Test
    public void findAuthorities_shouldFind() {
        assertThat(userService.findAuthorities(userAuthorityDaoTests.findAny().getUserId())).isNotEmpty();
    }

    @Test
    public void addAuthority_shouldAdd() {
        UserIdAuthorityId ua = userAuthorityDaoTests.findAuthorityToBeAdded();
        int initSize = userAuthorityDaoTests.countUserIdOccurences(ua.getUserId()).intValue();
        assertThat(initSize).isNotZero();
        User user = userService.addAuthority(ua.getUserId(), ua.getAuthorityId());
        assertThat(user).isNotNull();
        assertThat(user.getAuthorities().stream().anyMatch(a -> a.getId().equals(ua.getAuthorityId()))).isTrue();
        Assertions.assertThat(user.getAuthorities()).hasSize(initSize + 1);
        Assertions.assertThat(user.getAuthorities()).hasSize(userAuthorityDaoTests.countUserIdOccurences(ua.getUserId()).intValue());
    }

    @Test(expected = ValidationException.class)
    public void addAuthority_shouldFailCuzAlreadyAdded() {
        UserIdAuthorityId any = userAuthorityDaoTests.findAny();
        userService.addAuthority(any.getUserId(), any.getAuthorityId());
        fail("should have failed at this point");
    }

    @Test
    public void removeAuthority_shouldRemove() {
        UserIdAuthorityId ua = userAuthorityDaoTests.findAny();
        int initSize = userAuthorityDaoTests.countUserIdOccurences(ua.getUserId()).intValue();
        assertThat(initSize).isNotZero();
        User user = userService.removeAuthority(ua.getUserId(), ua.getAuthorityId());
        assertThat(user).isNotNull();
        assertThat(user.getAuthorities().stream().anyMatch(a -> a.getId().equals(ua.getAuthorityId()))).isFalse();
        Assertions.assertThat(user.getAuthorities()).hasSize(initSize - 1);
        Assertions.assertThat(user.getAuthorities()).hasSize(userAuthorityDaoTests.countUserIdOccurences(ua.getUserId()).intValue());
    }

    @Test(expected = EntityNotFoundException.class)
    public void removeAuthority_shouldFailCuzNoAuthority() {
        userService.removeAuthority(userAuthorityDaoTests.findAny().getUserId(), 9999L);
        fail("should have failed at this point");
    }

    @Test(expected = ValidationException.class)
    public void removeAuthority_shouldFailCuzNoAuthorityForThatUser() {
        UserIdAuthorityId ua = userAuthorityDaoTests.findAuthorityToBeAdded();
        userService.removeAuthority(ua.getUserId(), ua.getAuthorityId());
        fail("should have failed at this point");
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

}
