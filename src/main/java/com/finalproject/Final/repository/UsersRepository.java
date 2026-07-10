package com.finalproject.Final.repository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.UserBean;






@Repository
public class UsersRepository {
  @Autowired
  JdbcTemplate jdbc;
  
  public int insertUser(UserBean obj) {
    int i=0;
    
    
    String sql = " INSERT INTO user\r\n"
    		+ "                (userID, roleID, name, email, password,\r\n"
    		+ "                 phone_no, address, dob, gender, profile_image, is_active, created_at, updated_at)\r\n"
    		+ "                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
i= jdbc.update(
                sql,
                obj.getUserId(),
                obj.getRoleId(),
                obj.getName(),
                obj.getEmail(),
               obj.getPassword(),
                obj.getPhoneNumber(),
                obj.getAddress(),
                obj.getDob(),
                obj.getGender(),
                obj.getProfileImage(), 
                obj.getIsActive(), 
                obj.getCreatedAt(), 
                obj.getUpdatedAt()
        );
   
  return i;
}
  
  public String getRoleIdByName(String roleName) {

	    String sql = "SELECT roleID FROM role WHERE name = ?";

	    return jdbc.queryForObject(
	            sql,
	            String.class,
	            roleName
	    );
	}
  
  public boolean existsByEmail(String email) {

      String sql = "SELECT COUNT(*) FROM user WHERE email = ?";
      Integer count = jdbc.queryForObject(sql, Integer.class,email);

      return count != null && count > 0;
  }

  
  @SuppressWarnings("deprecation")
  public UserBean getLatestUserByRole(String roleName) {

	    String roleId = getRoleIdByName(roleName);

	    String sql = "  SELECT *\r\n"
	    		+ "	            FROM user\r\n"
	    		+ "	            WHERE roleID = ?\r\n"
	    		+ "	            ORDER BY created_at DESC\r\n"
	    		+ "	            LIMIT 1";
	          
	            


	    return jdbc.queryForObject(
	            sql,
	            (rs, rowNum) -> {

	                UserBean userObj = new UserBean();

	                userObj.setUserId(rs.getString("userID"));
	                userObj.setRoleId(rs.getString("roleID"));
	                userObj.setName(rs.getString("name"));
	                userObj.setEmail(rs.getString("email"));
	                userObj.setPassword(rs.getString("password"));
	                userObj.setPhoneNumber(rs.getString("phone_no"));
	                userObj.setAddress(rs.getString("address"));

	                if(rs.getDate("dob") != null) {
	                    userObj.setDob(
	                        rs.getDate("dob").toLocalDate()
	                    );
	                }

	                userObj.setGender(rs.getString("gender"));
	                userObj.setProfileImage(rs.getString("profile_image"));
	                userObj.setIsActive(rs.getInt("is_active"));

	                if(rs.getTimestamp("created_at") != null) {
	                    userObj.setCreatedAt(
	                        rs.getTimestamp("created_at").toLocalDateTime()
	                    );
	                }

	                if(rs.getTimestamp("updated_at") != null) {
	                    userObj.setUpdatedAt(
	                        rs.getTimestamp("updated_at").toLocalDateTime()
	                    );
	                }


	                return userObj;
	            },
	            roleId
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
	                userObj.getUserId()
	        );
	        
	  }
  
  @SuppressWarnings("deprecation")
public UserBean getUserByEmail(String email) {

      String sql = "SELECT * FROM user WHERE email=?";
  try {

                return jdbc.queryForObject(sql, new Object[] { email }, (rs, rowNum) -> {

                    UserBean userObj = new UserBean();
                    userObj.setUserId(rs.getString("userID"));
                    userObj.setRoleId(rs.getString("roleID"));
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

              userObj.setUserId(rs.getString("userID"));
              userObj.setRoleId(rs.getString("roleID"));
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

  
  }

    
  
  

     
