package com.finalproject.Final.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseCategoryBean {

    private String CourseCategoryId;
    private String name;
    private String description;
    private int isActive;
    private LocalDateTime createdAt; 
}