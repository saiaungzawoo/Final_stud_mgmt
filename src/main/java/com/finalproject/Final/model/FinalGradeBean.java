package com.finalproject.Final.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FinalGradeBean {

    private String finalGradeID;

    private String enrollmentID;

    private Double assignmentTotalScore;

    private Double examTotalScore;

    private Double attendanceScore;

    private Double finalScore;

    private String letterGrade;

    private String status;

    private String remarks;

    private String finalizedByID;

    private LocalDateTime finalizedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


   
}