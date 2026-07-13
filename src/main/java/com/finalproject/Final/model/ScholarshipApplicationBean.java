package com.finalproject.Final.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScholarshipApplicationBean {


    private String scholarshipApplicationID;
    
    private String scholarshipName;
    private String scholarshipID;

    private String userID;
 
    private LocalDate applicationDate;// Application Information

    private String reason;
    
 // Upload File
    // Only used in form upload
    private MultipartFile file;
 
    private String supportingDocuments;// Database file path
 
    private String status;  // Admin Review
    // Pending, Approved, Rejected

    private String reviewedByID;

    private LocalDateTime reviewedAt;

    private String reviewNotes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    
    private String userName;

    private String email;

    private String phoneNo;

    private String address;
}

