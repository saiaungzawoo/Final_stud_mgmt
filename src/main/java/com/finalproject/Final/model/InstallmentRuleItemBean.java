package com.finalproject.Final.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InstallmentRuleItemBean {

	
	private String installmentRuleItemId;
	private String installmentRuleId;
	private Integer installmentNumber;
	private Double amount;
	private LocalDate dueDate;
	private LocalDateTime createdAt;
	
}
