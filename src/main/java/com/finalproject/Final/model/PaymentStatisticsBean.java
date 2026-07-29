package com.finalproject.Final.model;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaymentStatisticsBean {


    private Double collectedAmount;

    private Double outstandingAmount;

    private Integer pendingPayment;


}