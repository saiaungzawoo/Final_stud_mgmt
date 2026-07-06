package com.finalproject.Final.model;


import java.sql.Timestamp;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherBean {
	private int id;
	 private int roleId;
	@NotBlank(message = "Please enter your  name.")
	    private String name;
	@NotBlank(message = "Please enter your email. ")
	    private String email;
	@NotBlank(message = "Please create your password.")
	    private String password;
	@NotBlank(message = "Please enter your phone number.")
	    private String phoneNo;
	@NotBlank(message = "Please enter your address.")
	    private String address;
	@NotBlank(message = "Please select your date of birth.")
	    private String dob;
	@NotBlank(message = "Please select your Gender.")
	    private String gender;
	
		private Timestamp created_at;
	
	    private int isActive;
	
	    private String filePath;
	

}
