package com.finalproject.Final.dto;

import java.util.ArrayList;
import java.util.List;

import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.model.InstallmentRuleItemBean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseCreateRequest {

    private CourseBean course = new CourseBean();

    private Integer installmentCount;
    
    private String installmentRuleName;

    private List<InstallmentRuleItemBean> installmentItems;
    
    public CourseCreateRequest() {
        installmentItems = new ArrayList<>();
    }

}