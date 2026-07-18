package com.finalproject.Final.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.finalproject.Final.model.SubCategoryBean;
import com.finalproject.Final.repository.SubCategoryRepository;
import com.finalproject.Final.service.CourseCategoryService;
import com.finalproject.Final.service.CourseService;
import com.finalproject.Final.service.SubCategoryService;

@Controller
public class IndexController {

    @Autowired
    private CourseCategoryService courseCatService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private SubCategoryService subCatService;
    
    @GetMapping("/")
    public String home(Model model) {
    	
    	model.addAttribute("allCategories",
    			courseCatService.getAllCategories());

    	model.addAttribute("courses",
    	        courseService.getAllCourses());
    	
    	model.addAttribute(
    	        "allSubcategories",
    	        subCatService.getAll()
    	);
    	return "layout/index";
    }

	/*
	 * @GetMapping("/") public String showHomePage(Model model) {
	 * 
	 * List<SubCategoryBean> allList = subRepo.getAllSubCategory();
	 * 
	 * model.addAttribute("allList", allList);
	 * 
	 * 
	 * return "layout/index"; }
	 */

}