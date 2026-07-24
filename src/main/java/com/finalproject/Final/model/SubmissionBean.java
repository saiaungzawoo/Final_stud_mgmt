package com.finalproject.Final.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class SubmissionBean {

    private String submissionID;

    private String assignmentID;

    private String userID;

    // Teacher view အတွက် join ကနေယူမယ့် student name
    private String studentName;

    private String filePath;

    private String submissionText;

    private BigDecimal score;

    private String feedback;

    private String gradedByID;

    private LocalDateTime gradedAt;

    private LocalDateTime submittedAt;

    private LocalDateTime updatedAt;
    
    private String assignmentTitle; 
    
    //thiri
        private String courseName;
private String courseID;
private String createdByID;
private String description;
private BigDecimal weightPercent;
private LocalDateTime dueDate;
private String remarks;
private String title;
private String status; 
private LocalDateTime createdAt;
private LocalDateTime AssignupdatedAt;
private MultipartFile file;
private boolean submitted;

public boolean isSubmitted() {
    return submitted;
}
public void setSubmitted(boolean submitted) {
    this.submitted = submitted;
}
     
}