package com.soprasteria.initiatives.auth.dao;

import com.soprasteria.initiatives.auth.domain.Authority;
import com.soprasteria.initiatives.auth.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

/**
 * DAO containing utility methods to test join between {@link User} and {@link Authority} entity
 *
 * @author jntakpe
 */
@Repository
@Transactional(readOnly = true)
public class UserAuthorityDaoTests {

    private final JdbcTemplate jdbcTemplate;

    private final UserDaoTests userDaoTests;

    private final AuthorityDaoTests authorityDaoTests;

    @Autowired
    public UserAuthorityDaoTests(DataSource dataSource, UserDaoTests userDaoTests, AuthorityDaoTests authorityDaoTests) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.userDaoTests = userDaoTests;
        this.authorityDaoTests = authorityDaoTests;
    }

    public UserIdAuthorityId findAuthorityToBeAdded() {
        List<UserIdAuthorityId> allUAIds = findAll();
        for (Long userId : userDaoTests.findAllIds()) {
            for (Long authorityId : authorityDaoTests.findAllIds()) {
                UserIdAuthorityId currentUAId = new UserIdAuthorityId(userId, authorityId);
                if (!allUAIds.contains(currentUAId)) {
                    return currentUAId;
                }
            }
        }
        throw new IllegalStateException("No free UserAuthorities remaining");
    }

    public UserIdAuthorityId findAny() {
        return findAll().stream().findAny().orElseThrow(() -> new IllegalStateException("No UserAuthorities"));
    }

    public Long countUserIdOccurences(Long userId) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM user_authorities WHERE user_id=" + userId, Long.class);
    }

    public Long countAuthorityIdOccurences(Long authorityId) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM user_authorities WHERE authority_id=" + authorityId, Long.class);
    }

    private List<UserIdAuthorityId> findAll() {
        return jdbcTemplate.query("SELECT * FROM user_authorities",
                (rs, rowNum) -> new UserIdAuthorityId(rs.getLong("user_id"), rs.getLong("authority_id")));
    }

}
