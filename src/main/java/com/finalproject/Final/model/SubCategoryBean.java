package com.finalproject.Final.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubCategoryBean {

    private int id;
    private String name;
    private String description;

    private int courseCategoryId;
}