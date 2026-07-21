package com.finalproject.Final.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.finalproject.Final.service.CourseCategoryService;



@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private CourseCategoryService categoryService;

    @ModelAttribute("allCategories")
    public Object categories(){

        return categoryService.getAllCategories();

    }

}