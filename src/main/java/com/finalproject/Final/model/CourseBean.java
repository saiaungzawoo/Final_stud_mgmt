package com.finalproject.Final.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseBean {

    private String CourseId;
    private String userId;

    private String courseCategoryId;
    private String subcategoryId;

    private String name;
    private String description;
    
    private Integer durationWeeks;
    
    private String subcategoryName;
    private String categoryName;	
    private String teacherId;
    private String teacherName;

  

    
    private double fee;

    private String level;
    private String status;

    private String createdBy;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String thumbnailPath;
    
    private Integer allowedInstallment;
    
    private Integer allowedScholarship;
    
    private Integer seatsTotal;
    private Integer seatsAvailable;
}