package com.finalproject.Final.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.UsersBean;

<<<<<<< Updated upstream
import jakarta.validation.Valid;



=======
>>>>>>> Stashed changes
@Repository
public class UsersRepository {
	@Autowired
	JdbcTemplate jdbc;
	
<<<<<<< Updated upstream
	public int insertUser(UsersBean obj) {
		int i=0;
		
		
		String sql="INSERT INTO `student_mgmt_v6`.`user` (`role_id`, `name`, `email`, `password`, `phone_no`, `address`,`dob`, `gender`,`created_at`,`is_active`,`file_path`)\r\n"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		
		i=jdbc.update(sql,obj.getRoleId(),obj.getName(),obj.getEmail(),obj.getPassword()
				,obj.getPhoneNumber(),obj.getAddress(),obj.getDob(),
				obj.getGender(),obj.getCreatedAt(),obj.getIsActive(),obj.getFilePath());
	
		return i;
	}
=======
	public int insertUser(UserBean obj) {
		int i=0;
		
		
		String sql = """
                INSERT INTO user
                (userID, roleID, name, email, password,
                 phone_no, address, dob, gender, profile_image)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
i= jdbc.update(
                sql,
                obj.getUserID(),
                obj.getRoleID(),
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
	
>>>>>>> Stashed changes
	
	public boolean existsByEmail(String email) {

	    String sql = "SELECT COUNT(*) FROM user WHERE email = ?";
	    Integer count = jdbc.queryForObject(sql, Integer.class,email);
<<<<<<< Updated upstream

	    return count != null && count > 0;
	}

	
	public UserBean getLatestStudent() {

	    String sql = "SELECT * FROM user WHERE role_id = 3 ORDER BY id DESC LIMIT 1";

	    return jdbc.queryForObject(
	            sql,
	            (rs, rowNum) -> new UserBean(
	            		rs.getInt("id"),
	                    rs.getInt("role_id"),
	                    rs.getString("name"),
	                    rs.getString("email"),
	                    rs.getString("password"),
	                    rs.getString("phone_no"),
	                    rs.getString("address"),
	                    rs.getDate("dob").toLocalDate(),
	                    rs.getString("gender"),
	                    rs.getTimestamp("created_at").toLocalDateTime(),
	                    rs.getInt("is_active"),
	                    rs.getString("file_path")
	            )
	    );
	}
	
	
	
	           
	public int updateUser(UserBean userObj) {
		
		//String sql="update user set"
			//	+ " name=?,email=?,password=?,"
			//	+ " phone_no=?,address=?,dob=?,gender=?,file_path=? where id=?";
		
		
		String sql="UPDATE `user` SET `name` = ?, `email` = ?,"
				+ " `password` = ?, `phone_no` = ?, `address` = ?, `dob` = ?, "
				+ "`gender` = ?, `file_path` = ? WHERE (`id` = ?)";
			
			return	jdbc.update(sql,userObj.getName(),userObj.getEmail(),
					userObj.getPassword(),userObj.getPhoneNumber(),
					userObj.getAddress(),userObj.getDob(),userObj.getGender()
					,userObj.getFilePath(),userObj.getId());
		
		
	}
	      
	
	public UserBean getUserByEmail(String email) {

	    String sql = "SELECT * FROM user WHERE email=?";

	    try {
	        return jdbc.queryForObject(
	                sql,
	                new BeanPropertyRowMapper<>(UserBean.class),
	                email);

	    } catch (Exception e) {
	        return null;
	    }
	}
	   
	
	public UserBean getUserById(int id) {

	    String sql = "SELECT * FROM user WHERE id = ?";

	    try {

	        return jdbc.queryForObject(
	                sql,
	                new BeanPropertyRowMapper<>(UserBean.class),
	                id);

	    } catch (Exception e) {

	        return null;

	    }

	}
}
		
	
	



=======

	    return count != null && count > 0;
	}

	
	public UserBean getLatestStudent() {
		String studentRoleId = "19dac244-7acd-11f1-898e-e4b97a5cf834";
	   
	    
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
	
	
	public UserBean getUserByEmail(String email) {

	    String sql = "SELECT * FROM user WHERE email=?";
  try {

	    	        return jdbc.queryForObject(sql, new Object[] { email }, (rs, rowNum) -> {

	    	            UserBean userObj = new UserBean();

	    	            userObj.setUserID(rs.getString("userID"));
	    	            userObj.setRoleID(rs.getString("roleID"));
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
	   
	
	public UserBean getUserById(String userID) {

	    String sql = "SELECT * FROM user WHERE userID = ?";

	    try {

	        return jdbc.queryForObject(sql, new Object[]{userID}, (rs, rowNum) -> {

	            UserBean userObj = new UserBean();

	            userObj.setUserID(rs.getString("userID"));
	            userObj.setRoleID(rs.getString("roleID"));
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

		
	
	




    
  
  

     
>>>>>>> Stashed changes
