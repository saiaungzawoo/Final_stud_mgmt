package com.finalproject.Final.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter 
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionBean {
	private String submissionID;
    private String assignmentID;
    private String userID;

    private String filePath;

    private MultipartFile file;
    
    
@NotBlank(message ="Answer is required")
    private String submissionText;

    private BigDecimal score;

    private String feedback;

    private String gradedByID;

    private LocalDateTime gradedAt;

    private LocalDateTime submittedAt;

    private LocalDateTime updatedAt;

    // For View
    private String studentName;
    private String assignmentTitle;
    private String teacherName;

    
    
  //use for assignment 
    private String courseID;
    private String courseName;
    private String createdByID;

    private String title;
    private String description;

    private BigDecimal maxScore;
    private BigDecimal weightPercent;

    private LocalDateTime dueDate;

    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime assignupdatedAt;
}

