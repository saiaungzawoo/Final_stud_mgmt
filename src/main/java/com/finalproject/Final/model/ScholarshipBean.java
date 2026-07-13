package com.finalproject.Final.model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter 
@AllArgsConstructor
@NoArgsConstructor
public class ScholarshipBean {

	private String scholarshipID;
	
	private String courseID;
	
	 private String courseName;
	 
	private String scholarshipName;
	
	private String description;
	
	private String discountType;
	
	private String discountValue;
	
	 private Integer maxRecipients;
	
	private  LocalDate applicationDeadline;
	
	private Integer isActive;
	
	private  String createdByUserID;
	 
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
	
	
	
	
}
