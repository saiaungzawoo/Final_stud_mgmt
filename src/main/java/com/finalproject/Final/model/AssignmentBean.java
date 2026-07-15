package com.finalproject.Final.model;




	import java.math.BigDecimal;
	import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
	import lombok.Getter;
	import lombok.NoArgsConstructor;
	import lombok.Setter;

	@Setter
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public class AssignmentBean {

	    private String assignmentID;
@NotBlank(message="Please select course")
	    private String courseID;

	    private String createdByID;
	    @NotBlank(message = "Title is required")
	    private String title;
	    
	    @NotBlank(message = "Description is required")
	    private String description;
	    
	    @NotNull(message = "Max score is required")
	    private BigDecimal maxScore;
	    
	    @NotNull(message = "Weight percent is required")
	    @DecimalMin(value = "0", message = "Weight must be between 0 and 100")
	    @DecimalMax(value = "100", message = "Weight must be between 0 and 100")
	    
	    private BigDecimal weightPercent;
	    @NotNull(message = "Due date is required")
	    
	    private LocalDateTime dueDate;
	    @NotNull(message = "Status is required")
	    
	    private AssignmentStatus status;

	    private LocalDateTime createdAt;

	    private LocalDateTime updatedAt;
	    // Database column မဟုတ်ဘူး
	    private String CourseName;

	}

