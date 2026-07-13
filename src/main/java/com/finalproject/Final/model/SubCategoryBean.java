package com.finalproject.Final.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubCategoryBean {

    private String SubCategoryId;
    private String name;
    private String description;
    private Integer isActive;

    private String courseCategoryId;
    private LocalDateTime createdAt;
}