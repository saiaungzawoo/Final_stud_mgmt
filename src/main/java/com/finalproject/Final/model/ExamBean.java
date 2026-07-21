package com.finalproject.Final.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamBean {
	
	    private String examID;
	    @NotBlank(message = "Please select course")
	    private String courseID;

	    private String createdByID;
	    @NotBlank(message = "Exam name is required")
	    private String name;
	    @NotBlank(message = "Please select exam type")
	    private String examType;
	    @NotNull(message = "Maximum score is required")
	    @DecimalMin(value = "0.01", message = "Maximum score must be greater than 0")
	    private BigDecimal maxScore;
	    @NotNull(message = "Weight percent is required")
	    @DecimalMin(value = "0.01", message = "Weight must be greater than 0")
	    @DecimalMax(value = "100.00", message = "Weight cannot exceed 100")
	    private BigDecimal weightPercent;
	    @NotNull(message = "Exam date is required")
	    private LocalDateTime examDate;
	    @NotNull(message = "Duration is required")
	    @Min(value = 1, message = "Duration must be at least 1 minute")
	    private Integer durationMinutes;
	    
	    private String status;
	    private String CourseName;
	    private Boolean cancelled;
	    public String getCurrentStatus() {

	    	  if("Cancelled".equals(status)){
	    	        return "Cancelled";
	    	    }

	        LocalDateTime now = LocalDateTime.now();

	        LocalDateTime endTime = examDate.plusMinutes(durationMinutes);

	        if(now.isBefore(examDate)){
	            return "Scheduled";
	        }
	        else if(now.isAfter(endTime)){
	            return "Completed";
	        }
	        else{
	            return "In Progress";
	        }
	    }
	}

