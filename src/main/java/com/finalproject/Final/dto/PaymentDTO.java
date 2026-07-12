package com.finalproject.Final.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaymentDTO {


private String enrollmentId;

private String paymentTypeId;

private String paymentMethodId;

private Double amount;

private String installmentRuleId;


}