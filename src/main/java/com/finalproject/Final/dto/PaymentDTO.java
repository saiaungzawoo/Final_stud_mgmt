package com.finalproject.Final.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaymentDTO {

    private int enrollmentId;
    private int userId;
    private int courseId;

    private int amount;
    private String paymentMethod;  // KBZ, AYA, CB, Card
    private String paymentType;    // FULL, SCHOLARSHIP
}