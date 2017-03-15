package com.soprasteria.initiatives.auth.service;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * Utilities for business services testing
 *
 * @author jntakpe
 */
public abstract class AbstractDBServiceTests {

    int nbEntries;

    JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void setUp() throws Exception {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Before
    public void beforeEach() {
        nbEntries = countRowsInCurrentTable();
    }

    int countRowsInCurrentTable() {
        return JdbcTestUtils.countRowsInTable(jdbcTemplate, getTableName());
    }

    protected abstract String getTableName();
}
