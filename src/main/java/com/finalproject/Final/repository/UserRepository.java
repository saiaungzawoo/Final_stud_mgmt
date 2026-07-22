package com.finalproject.Final.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.UserBean;

@Repository
public class UserRepository {

	//test
    @Autowired
    private JdbcTemplate jdbc;
    //wont delete any codes, i will just comment codes

    private final UserRowMapper mapper = new UserRowMapper();
    
//    public UserBean findByEmail(String email) {
//        String sql = "SELECT * FROM user WHERE email = ?";
//        try {
//            return jdbc.queryForObject(
//                    sql,
//                    new BeanPropertyRowMapper<>(UserBean.class),
//                    email);
//        } catch (Exception e) {
//            return null;
//        }
//    }
    
    //sai
    // to get role name 
    public UserBean findByEmail(String email) {

        String sql = """
                
                SELECT 
                    u.*,
                    r.name AS roleName

                FROM user u

                JOIN role r
                ON u.roleID = r.roleID

                WHERE u.email = ?

                """;


        try {

            return jdbc.queryForObject(
                    sql,
                    new UserRowMapper(),
                    email
            );

        } catch(Exception e) {

            return null;
        }
    }
    
    
    public int updatePassword(String email, String newPassword) {
        String sql = "UPDATE user SET password = ? WHERE email = ?";
        return jdbc.update(sql, newPassword, email);
    }

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