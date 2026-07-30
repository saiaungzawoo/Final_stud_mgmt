package com.finalproject.Final.model;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AdminEnrollmentDetailBean {


    /*
     * Enrollment
     */

    private String enrollmentId;

    private LocalDate enrollmentDate;

    private String enrollmentStatus;

    private String paymentStatus;



    /*
     * Student
     */

    private String studentId;

    private String studentName;

    private String studentEmail;

    private String studentPhone;



    /*
     * Course
     */

    private String courseId;

    private String courseName;

    private Double courseFee;



    /*
     * Teacher
     */

    private String teacherName;

    private String teacherEmail;



    /*
     * Payment Summary
     */

    private Double finalFee;

    private Double totalPaid;

    private Double remainingBalance;
    
    private Double originalFee;

    private Double discountAmount;
    
    private String paymentType;



    /*
     * Installment
     */

    private Integer totalInstallments;

    private Integer completedInstallments;
    
    private LocalDateTime updatedAt;
    
    private LocalDateTime createdAt;


    private LocalDate paymentDate;
    
    private String statusReason;
    
    
    public boolean isInstallmentPayment() {

        return totalInstallments != null 
                && totalInstallments > 0;

    }



}