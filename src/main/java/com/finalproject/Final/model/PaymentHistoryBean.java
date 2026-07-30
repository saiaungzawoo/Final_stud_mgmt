package com.finalproject.Final.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentHistoryBean {


    private String paymentId;

    private Double amount;

    private String paymentMethodName;

    private LocalDate paymentDate;

    private String status;

    private String transactionReference;


}