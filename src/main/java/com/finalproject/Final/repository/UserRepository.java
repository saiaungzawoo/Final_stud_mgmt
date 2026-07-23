package com.finalproject.Final.repository;


import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.UserBean;
@Repository
public class UserRepository {
    private final JdbcTemplate jdbc;
    private final UserRowMapper mapper =
            new UserRowMapper();
    public UserRepository(
            JdbcTemplate jdbc
    ) {

        this.jdbc = jdbc;

    }
    /**
     * Find user by email
     */
    public UserBean findByEmail(String email) {


        String sql = """
                SELECT
                    userID,
                    roleID,
                    name,
                    email,
                    password,
                    phone_no,
                    address,
                    dob,
                    gender,
                    profile_image,
                    otp_code,
                    is_active,
                    created_at,
                    updated_at
                FROM user
                WHERE email = ?
                """;



        try {

            return jdbc.queryForObject(
                    sql,
                    mapper,
                    email
            );


        } catch(Exception e) {

            return null;

        }

    }
    /**
     * Check email exists
     */
    public boolean checkUser(String email) {


        String sql =
                """
                SELECT COUNT(*)
                FROM user
                WHERE email = ?
                """;



        Integer count =
                jdbc.queryForObject(
                        sql,
                        Integer.class,
                        email
                );


        return count != null && count > 0;

    }

    /**
     * Update password
     */
    public int updatePassword(
            String email,
            String password
    ) {


        String sql =
                """
                UPDATE user
                SET password = ?
                WHERE email = ?
                """;



        return jdbc.update(
                sql,
                password,
                email
        );

    }

    /**
     * Save OTP Code
     */
    public int saveOtpCode(
            String email,
            String otp
    ) {


        String sql =
                """
                UPDATE user
                SET otp_code = ?
                WHERE email = ?
                """;



        return jdbc.update(
                sql,
                otp,
                email
        );

    }

    /**
     * Clear OTP Code
     */
    public int clearOtpCode(
            String email
    ) {


        String sql =
                """
                UPDATE user
                SET otp_code = NULL
                WHERE email = ?
                """;



        return jdbc.update(
                sql,
                email
        );

    }
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

