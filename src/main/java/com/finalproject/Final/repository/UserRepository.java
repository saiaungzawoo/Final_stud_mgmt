package com.finalproject.Final.repository;

import java.beans.BeanProperty;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.UserBean;


@Repository
public class UserRepository {


    private final JdbcTemplate jdbc;

    private final UserRowMapper mapper = new UserRowMapper();



    public UserRepository(JdbcTemplate jdbc){

        this.jdbc = jdbc;

    }



    // =====================================
    // Find User By Email
    // Login + Forgot Password OTP
    // =====================================

    public UserBean findByEmail(String email){


        String sql = """
                SELECT
                    userID,
                    roleID,
                    name,
                    email,
                    password,
                    phone_no,
                    otp_code,
                    otp_created_at,
                    address,
                    dob,
                    gender,
                    profile_image,
                    is_active,
                    created_at,
                    updated_at
                FROM user
                WHERE email = ?
                """;


        try{

            return jdbc.queryForObject(
                    sql,
                    mapper,
                    email
            );


        }catch(Exception e){

            return null;

        }

    }





    // =====================================
    // Check Email Exists
    // =====================================

    public boolean checkUser(String email){


        String sql = """
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





    // =====================================
    // Save OTP
    // =====================================

    public int saveOtpCode(
            String email,
            String otp
    ){


        String sql = """
                UPDATE user
                SET
                    otp_code = ?,
                    otp_created_at = NOW()
                WHERE email = ?
                """;


        int result =
                jdbc.update(
                        sql,
                        otp,
                        email
                );
        return result;

    }
    // =====================================
    // Verify OTP
    // =====================================

    public UserBean findByEmailAndOtp(
            String email,
            String otp
    ){


        String sql = """
                SELECT
                    userID,
                    roleID,
                    name,
                    email,
                    password,
                    phone_no,
                    otp_code,
                    otp_created_at,
                    address,
                    dob,
                    gender,
                    profile_image,
                    is_active,
                    created_at,
                    updated_at
                FROM user
                WHERE email = ?
                AND otp_code = ?
                """;


        try{

            return jdbc.queryForObject(
                    sql,
                    mapper,
                    email,
                    otp
            );


        }catch(Exception e){

            return null;

        }

    }






    // =====================================
    // Clear OTP
    // =====================================

    public int clearOtpCode(String email){


        String sql = """
                UPDATE user
                SET
                    otp_code = NULL,
                    otp_created_at = NULL
                WHERE email = ?
                """;


        return jdbc.update(
                sql,
                email
        );

    }
// =====================================
    // Update Password
    // =====================================

    public int updatePassword(
            String email,
            String password
    ){


        String sql = """
                UPDATE user
                SET
                    password = ?
                WHERE email = ?
                """;


        return jdbc.update(
                sql,
                password,
                email
        );

    }







    // =====================================
    // Find User By ID
    // =====================================

    public UserBean findById(String userID){


        String sql = """
                SELECT
                    u.userID,
                    u.roleID,
                    r.roleName,
                    u.name,
                    u.email,
                    u.password,
                    u.phone_no,
                    u.address,
                    u.dob,
                    u.gender,
                    u.profile_image,
                    u.otp_code,
                    u.otp_created_at,
                    u.is_active,
                    u.created_at,
                    u.updated_at

                FROM user u

                LEFT JOIN role r
                ON u.roleID = r.roleID

                WHERE u.userID = ?
                """;


        try{

            return jdbc.queryForObject(
                    sql,
                    mapper,
                    userID
            );


        }catch(Exception e){

            return null;

        }


    }

 // GET USER BY ID
    public UserBean findById(int id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        try {
            return jdbc.queryForObject(sql, new  BeanPropertyRowMapper<>(UserBean.class), id);
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

