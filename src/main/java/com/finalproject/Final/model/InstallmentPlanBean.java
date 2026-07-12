package com.finalproject.Final.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InstallmentPlanBean {

	
	private String installmentPlanId;
	
	private String enrollmentId;
	private String installmentRuleItemId;
	private Integer installmentNumber;
	private Double amountDue;
	private LocalDate dueDate;
	private Double paidAmount;
	private String status;
	private LocalDateTime paidAt;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
}
