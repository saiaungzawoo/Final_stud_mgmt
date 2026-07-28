package com.finalproject.Final.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CertificateBean {


    private String certificateID;

    private String enrollmentID;

    private String certificateNumber;

    private LocalDate issueDate;

    private LocalDate expiryDate;

    private String finalGradeID;

    private String issuedByID;
    
    private String issuedByName;

    private String templatePath;

    private String pdfPath;

    private LocalDateTime createdAt;



    // Display purpose

    private String studentName;

    private String courseName;

    private String letterGrade;

    private BigDecimal finalScore;
    
    private String status;



  

}