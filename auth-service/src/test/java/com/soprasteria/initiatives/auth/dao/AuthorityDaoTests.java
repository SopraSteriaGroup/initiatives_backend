package com.soprasteria.initiatives.auth.dao;

import com.soprasteria.initiatives.auth.domain.Authority;
import com.soprasteria.initiatives.auth.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

/**
 * DAO containing utility methods to test {@link Authority} entity
 *
 * @author jntakpe
 */
@Repository
@Transactional(readOnly = true)
public class AuthorityDaoTests {

    private final AuthorityRepository authorityRepository;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthorityDaoTests(AuthorityRepository authorityRepository, DataSource dataSource) {
        this.authorityRepository = authorityRepository;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Authority findAny() {
        return authorityRepository.findAll().stream().findAny().orElseThrow(() -> new IllegalStateException("No authority"));
    }

    public Authority findAnyButThis(Authority authority) {
        return authorityRepository.findAll().stream()
                .filter(a -> !a.equals(authority))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("Different authority needed"));
    }

    public Long countByName(String name) {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM authority WHERE name='" + name + "'", Long.class);
    }

    public boolean exist(Long id) {
        Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM authority WHERE id=" + id, Long.class);
        return count == 1;
    }

    List<Long> findAllIds() {
        return jdbcTemplate.query("SELECT id FROM authority", (rs, rowNum) -> rs.getLong("id"));
    }
}
