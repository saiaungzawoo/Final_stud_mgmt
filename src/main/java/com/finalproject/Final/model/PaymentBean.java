package com.finalproject.Final.model;


import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentBean {

    private String paymentId;
    private String enrollmentId;
    private String installmentPlanId;
    private String paymentMethodId;
    private String paymentMethodName;
    
    private String paymentTypeId;
    private String paymentTypeName;
    
    private String studentName;

    private Double amount;

    private LocalDate paymentDate;

    private String transactionReference;

    private String status;

    private String notes;

    private String processedById;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    
    private Integer installmentNumber;

    private Integer installmentCount;
    
    private Double amountDue;
    private Double paidAmount;
    private LocalDate dueDate;

    
    
    private Double courseFee;

    private Double totalPaid;

    private Double remainingBalance;

    private Integer completedInstallments;

    private Integer totalInstallments;

    private Integer nextInstallmentNumber;

    private Double nextInstallmentAmount;

    private LocalDate nextDueDate;
    
	    

}