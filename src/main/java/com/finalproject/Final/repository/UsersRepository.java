package com.finalproject.Final.repository;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.UserBean;


@Repository
public class UsersRepository {
	@Autowired
	JdbcTemplate jdbc;
	//for student to insert
	public int insertUser(UserBean obj) {
		int i=0;
		
		
		String sql = """
                INSERT INTO user
                (userID, roleID, userCode, name, email, password,
                 phone_no, address, dob, gender, profile_image)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
i= jdbc.update(
                sql,
                obj.getUserID(),
                obj.getRoleID(),
                //SAI
                obj.getUserCode(),
                obj.getName(),
                obj.getEmail(),
               obj.getPassword(),
                obj.getPhoneNumber(),
                obj.getAddress(),
                obj.getDob(),
                obj.getGender(),
                obj.getProfileImage()
        );
   
	return i;
}
	
	//for student
	public boolean existsByEmail(String email) {

	    String sql = "SELECT COUNT(*) FROM user WHERE email = ?";
	    Integer count = jdbc.queryForObject(sql, Integer.class,email);

	    return count != null && count > 0;
	}

	
	@SuppressWarnings("deprecation")
	public UserBean getLatestStudent() {
		String studentRoleId = "3c2f4396-7a84-11f1-bfcb-b4b686e7f920";
	   
	    
	    String sql = """
	            SELECT *
	            FROM user
	            WHERE roleID = ?
	            ORDER BY created_at DESC
	            LIMIT 1
	            """;

	    return jdbc.queryForObject(sql,
	            new Object[] { studentRoleId },
	            (rs, rowNum) -> {

	                UserBean userObj = new UserBean();

	                userObj.setUserID(rs.getString("userID"));
	                userObj.setRoleID(rs.getString("roleID"));
	                //SAI
		            userObj.setUserCode(rs.getString("userCode"));
	                userObj.setName(rs.getString("name"));
	                userObj.setEmail(rs.getString("email"));
	                userObj.setPassword(rs.getString("password"));
	                userObj.setPhoneNumber(rs.getString("phone_no"));
	                userObj.setAddress(rs.getString("address"));
	                userObj.setDob(rs.getDate("dob").toLocalDate());
	                userObj.setGender(rs.getString("gender"));
	                userObj.setProfileImage(rs.getString("profile_image"));
	                userObj.setIsActive(rs.getInt("is_active"));
	                userObj.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
	                userObj.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

	                return userObj;
	            }
	          
	    		);
	}
	   
	
	
	
	           //for student edit
	public int updateUser(UserBean userObj) {
	
		String sql = " UPDATE user SET name = ?,email = ?,password = ?,"
		 		+ "phone_no = ?,address = ?, dob = ?,gender = ?,profile_image = ?"
		 		+ " WHERE userID = ?";
		            
		 return jdbc.update(
		            sql,
		            userObj.getName(),
		            userObj.getEmail(),
		            userObj.getPassword(),
		            userObj.getPhoneNumber(),
		            userObj.getAddress(),
		            userObj.getDob(),
		            userObj.getGender(),
		            userObj.getProfileImage(),
		            userObj.getUserID()
		    );
	      
	}
	
	
	@SuppressWarnings("deprecation")
	public UserBean getUserByEmail(String email) {

	    String sql = "SELECT * FROM user WHERE email=?";
  try {

	    	        return jdbc.queryForObject(sql, new Object[] { email }, (rs, rowNum) -> {

	    	            UserBean userObj = new UserBean();

	    	            userObj.setUserID(rs.getString("userID"));
	    	            userObj.setRoleID(rs.getString("roleID"));
	    	            //SAI
	    	            userObj.setUserCode(rs.getString("userCode"));
	    	            userObj.setName(rs.getString("name"));
	    	            userObj.setEmail(rs.getString("email"));
	    	            userObj.setPassword(rs.getString("password"));
	    	            userObj.setPhoneNumber(rs.getString("phone_no"));
	    	            userObj.setAddress(rs.getString("address"));

	    	            if (rs.getDate("dob") != null) {
	    	                userObj.setDob(rs.getDate("dob").toLocalDate());
	    	            }

	    	            userObj.setGender(rs.getString("gender"));

	    	            if (rs.getTimestamp("created_at") != null) {
	    	                userObj.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
	    	            }

	    	            if (rs.getTimestamp("updated_at") != null) {
	    	                userObj.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
	    	            }
  userObj.setIsActive(rs.getInt("is_active"));
	    	            userObj.setProfileImage(rs.getString("profile_image"));

	    	            return userObj;

	    	        });

	    	    } catch (Exception e) {
	    	        e.printStackTrace();
	    	        return null;
	    	    }
	    	}
	   
	
	@SuppressWarnings("deprecation")
	public UserBean getUserById(String userID) {

	    String sql = "SELECT * FROM user WHERE userID = ?";

	    try {

	        return jdbc.queryForObject(sql, new Object[]{userID}, (rs, rowNum) -> {

	            UserBean userObj = new UserBean();

	            userObj.setUserID(rs.getString("userID"));
	            userObj.setRoleID(rs.getString("roleID"));
	            //SAI
	            userObj.setUserCode(rs.getString("userCode"));
	            userObj.setName(rs.getString("name"));
	            userObj.setEmail(rs.getString("email"));
	            userObj.setPassword(rs.getString("password"));
	            userObj.setPhoneNumber(rs.getString("phone_no"));
	            userObj.setAddress(rs.getString("address"));
	            userObj.setDob(rs.getDate("dob").toLocalDate());
	            userObj.setGender(rs.getString("gender"));
	            userObj.setProfileImage(rs.getString("profile_image"));
	            userObj.setIsActive(rs.getInt("is_active"));
	            userObj.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

	            if (rs.getTimestamp("updated_at") != null) {
	            	userObj.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
	            }

	            return userObj;
	        });

	    } catch (Exception e) {
	        return null;
	    }
	}

	
	public boolean existsByEmailAndNotUserId(String email, String userId) {

	    String sql = """
	            SELECT COUNT(*)
	            FROM user
	            WHERE email = ?
	            AND userID <> ?
	            """;

	    Integer count = jdbc.queryForObject(
	            sql,
	            Integer.class,
	            email,
	            userId
	    );

	    return count != null && count > 0;
	}
	}

		
	
	
	

		
	
	






     
