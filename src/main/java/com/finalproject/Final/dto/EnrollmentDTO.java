package com.finalproject.Final.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnrollmentDTO {

    private String userId;
    private String courseId;
    private String paymentTypeId;
    private String installmentRuleId;
}