package com.finalproject.Final.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamResultBean {
	 private String examResultID;

	    private String examID;

	    private String userID;

	    private String studentName;
	    @NotNull(message = "Score is required")
	    @DecimalMin(
	        value = "0.0",
	        message = "Score cannot be negative"
	    )
	    private BigDecimal score;

	    private String remarks;

	    private String gradedByID;

	    private LocalDateTime gradedAt;

	    private LocalDateTime createdAt;
	    // For View Result
	    private String examName;

	    private String courseName;

	    private BigDecimal maxScore;

	    private String examType;
}