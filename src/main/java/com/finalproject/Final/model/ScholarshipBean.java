package com.finalproject.Final.model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
	 
	 @NotBlank(message = "Scholarship name is required")
	    @Pattern(
	        regexp = "^[A-Z][a-zA-Z ]*$",
	        message = "Scholarship name must start with capital letter and contain only letters"
	    )
	private String scholarshipName;
	 @NotBlank(message = "Description is required")
	private String description;
	 @NotBlank(message = "Discount type is required")
	private String discountType;
	 @NotBlank(message = "Discount value is required")
	    @Pattern(
	        regexp = "^\\d+(\\.\\d{1,2})?$",
	        message = "Discount value must be a valid number"
	    )
	private String discountValue;
	
	 @NotNull(message = "Maximum recipients is required")
	    @Min(value = 1, message = "Maximum recipients must be at least 1")
	 private Integer maxRecipients;
	
	 @NotNull(message = "Application deadline is required")
	    @FutureOrPresent(message = "Application deadline cannot be in the past")
	private  LocalDate applicationDeadline;
	
	private Integer isActive;
	
	private  String createdByUserID;
	 
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
	
	
	
	
}
