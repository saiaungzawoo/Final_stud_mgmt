package com.finalproject.Final.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.finalproject.Final.dto.AdminDashboardDTO;
import com.finalproject.Final.service.AdminDashboardService;
import com.finalproject.Final.service.CourseService;


@Controller
@RequestMapping("/admin")
public class AdminController {



	@Autowired
    private AdminDashboardService adminDashboardService;
	
	@Autowired
	private CourseService courseService;



	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		
		 System.out.println("ADMIN CONTROLLER RUNNING");


	    AdminDashboardDTO dashboard =
	            adminDashboardService.getDashboardData();



	    model.addAttribute(
	            "dashboard",
	            dashboard
	    );



	    model.addAttribute(
	            "recentPayments",
	            adminDashboardService.getRecentPayments()
	    );



	    return "admin/admin-dashboard";

	}
	
	
	

	//21.7.26
	@GetMapping("/courses")
	public String courseList(Model model) {


	    model.addAttribute(
	            "courses",
	            courseService.getAllCourses()
	    );
	    
	    model.addAttribute(
	            "totalCourses",
	            courseService.countCourses()
	    );


	    model.addAttribute(
	            "openCourses",
	            courseService.countCoursesByStatus("Open")
	    );


	    model.addAttribute(
	            "draftCourses",
	            courseService.countCoursesByStatus("Draft")
	    );


	    model.addAttribute(
	            "scheduledCourses",
	            courseService.countCoursesByStatus("In Progress")
	    );


	    return "admin/admin-course-list";

	}

}