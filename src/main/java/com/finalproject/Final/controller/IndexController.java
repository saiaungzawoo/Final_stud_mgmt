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
    private SubCategoryRepository subRepo;
    
    @GetMapping("/")
    public String home(Model m) {
    	return "layout/index";
    }

	
	  @GetMapping("/") public String showHomePage(Model model) {
	 
	  List<SubCategoryBean> allList = subRepo.getAllSubCategory();
	  
	 model.addAttribute("allList", allList);
	 
	 
	  return "layout/index"; }
	 

}