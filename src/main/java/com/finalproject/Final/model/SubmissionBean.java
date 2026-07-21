package com.finalproject.Final.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
}