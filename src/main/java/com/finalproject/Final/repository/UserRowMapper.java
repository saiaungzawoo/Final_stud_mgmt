package com.finalproject.Final.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.finalproject.Final.model.UserBean;


public class UserRowMapper implements RowMapper<UserBean> {


    @Override
    public UserBean mapRow(ResultSet rs, int rowNum) throws SQLException {


        UserBean u = new UserBean();


        u.setUserID(rs.getString("userID"));

        u.setRoleID(rs.getString("roleID"));


        u.setName(rs.getString("name"));

        u.setEmail(rs.getString("email"));

        u.setPassword(rs.getString("password"));


        u.setPhoneNumber(
                rs.getString("phone_no")
        );
        
        u.setOtpCode(
                rs.getString("otp_code")
        );

        u.setAddress(
                rs.getString("address")
        );



        if(rs.getDate("dob") != null) {

            u.setDob(
                rs.getDate("dob").toLocalDate()
            );

        }


        u.setGender(
                rs.getString("gender")
        );


        u.setProfileImage(
                rs.getString("profile_image")
        );


        u.setIsActive(
                rs.getInt("is_active")
        );



        if(rs.getTimestamp("created_at") != null) {

            u.setCreatedAt(
                rs.getTimestamp("created_at")
                .toLocalDateTime()
            );
        }



        if(rs.getTimestamp("updated_at") != null) {

            u.setUpdatedAt(
                rs.getTimestamp("updated_at")
                .toLocalDateTime()
            );
        }



        return u;
    }

}