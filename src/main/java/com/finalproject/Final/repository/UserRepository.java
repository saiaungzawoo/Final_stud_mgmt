package com.finalproject.Final.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.UserBean;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbc;

    private final UserRowMapper mapper = new UserRowMapper();

    // GET USER BY ID
    public UserBean findById(int id) {

        String sql = "SELECT * FROM user WHERE id = ?";

        return jdbc.queryForObject(sql, mapper, id);
    }

    // OPTIONAL: GET TEACHERS ONLY
    public UserBean findTeacherById(int id) {

        String sql = "SELECT * FROM user WHERE id = ? AND role_id = 2";

        return jdbc.queryForObject(sql, mapper, id);
    }
}