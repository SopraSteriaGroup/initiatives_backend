package com.soprasteria.initiatives.auth.dao;

import com.soprasteria.initiatives.auth.domain.User;
import com.soprasteria.initiatives.auth.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

/**
 * DAO containing utility methods to test {@link User} entity
 *
 * @author jntakpe
 */
@Repository
@Transactional(readOnly = true)
public class UserDaoTests {

    private final UserRepository userRepository;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoTests(UserRepository userRepository, DataSource dataSource) {
        this.userRepository = userRepository;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public User findAny() {
        return userRepository.findAll().stream().findAny().orElseThrow(() -> new IllegalStateException("No user"));
    }

    public User findAnyWithAuthorities() {
        User any = findAny();
        Hibernate.initialize(any.getAuthorities());
        return any;
    }

    public User findAnyButThis(User user) {
        return userRepository.findAll().stream()
                .filter(u -> !u.equals(user))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Different user needed"));
    }

    public Long countByUsername(String username) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM user WHERE username='" + username + "'", Long.class);
    }

    public Long countByPassword(String password) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM user WHERE password='" + password + "'", Long.class);
    }

    public boolean exist(Long id) {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM user WHERE id=" + id, Long.class);
        return count == 1;
    }

    List<Long> findAllIds() {
        return jdbcTemplate.query("SELECT id FROM user", (rs, rowNum) -> rs.getLong("id"));
    }
}
