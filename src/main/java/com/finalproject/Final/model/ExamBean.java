package com.finalproject.Final.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamBean {

    private String examID;
    private String courseID;
    private String createdByID;

    private String name;
    private String examType;

    private BigDecimal maxScore;
    private BigDecimal weightPercent;

    private LocalDateTime examDate;

    private Integer durationMinutes;

    private String status;

    // Join with Course table
    private String courseName;
}