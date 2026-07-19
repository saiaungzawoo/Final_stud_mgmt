package com.finalproject.Final.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class AdminPaymentDTO {

    private String studentName;

    private String courseName;

    private String paymentType;

    private Double amount;

    private String status;

    private LocalDate paymentDate;

}