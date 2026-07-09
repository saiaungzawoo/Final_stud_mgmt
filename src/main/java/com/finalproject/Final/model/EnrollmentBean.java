package com.finalproject.Final.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollmentBean {

	private String enrollmentId;
	private String userId;
	private String courseId;
	private String paymentTypeId;
	private String installmentRuleId;
	private String scholarshipApplicationId;
	private LocalDate enrollmentDate;
	private Double originalFee;
	private Double discountedAmount;
	private Double finalFee;
	private String paymentStatus;
	private String status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	
	// JOIN FIELD
    private String courseTitle;
    private String username;

	

	
	

}
