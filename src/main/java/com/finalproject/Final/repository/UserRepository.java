package com.finalproject.Final.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.UserBean;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbc;

<<<<<<< Updated upstream
    private final UserRowMapper mapper = new UserRowMapper();
=======
    public UserBean findByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        try {
            return jdbc.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<>(UserBean.class),
                    email);
        } catch (Exception e) {
            return null;
        }
    }

    public int updatePassword(String email, String newPassword) {
        String sql = "UPDATE user SET password = ? WHERE email = ?";
        return jdbc.update(sql, newPassword, email);
    }

    // Used by forgot-password flow to confirm the user exists
    // before issuing an OTP. Adjust column name if your schema
    // doesn't use "username" — check against UserBean's fields.
    public boolean checkUser(String username, String email) {
        String sql = "SELECT COUNT(*) FROM user WHERE username = ? AND email = ?";
        Integer count = jdbc.queryForObject(sql, Integer.class, username, email);
        return count != null && count > 0;
    }
>>>>>>> Stashed changes

    // GET USER BY ID
    public UserBean findById(int id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        try {
            return jdbc.queryForObject(sql, new BeanPropertyRowMapper<>(UserBean.class), id);
        } catch (Exception e) {
            return null;
        }
    }

    // OPTIONAL: GET TEACHERS ONLY
    public UserBean findTeacherById(int id) {
        String sql = "SELECT * FROM user WHERE id = ? AND role_id = 2";
        try {
            return jdbc.queryForObject(sql, new BeanPropertyRowMapper<>(UserBean.class), id);
        } catch (Exception e) {
            return null;
        }
    }
}