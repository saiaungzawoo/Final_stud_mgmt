package com.finalproject.Final.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InstallmentRuleBean {

	
	private String installmentRuleId;
	private String courseId;
	private String name;
	private Integer installmentCount;
	private Integer isActive;
	private String createdById;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
}
