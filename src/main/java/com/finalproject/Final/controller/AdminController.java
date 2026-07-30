package com.finalproject.Final.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.finalproject.Final.dto.AdminDashboardDTO;
import com.finalproject.Final.model.CourseBean;
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
	public String courseList( @RequestParam(required = false) String keyword,
			 @RequestParam(required = false) String status,
			Model model) {


//		List<CourseBean> courses =
//		        courseService.getAllCoursesForAdmin();
		
		List<CourseBean> courses;


		if((keyword != null && !keyword.isBlank()) 
		        || (status != null && !status.isBlank())) {


		    courses =
		        courseService.searchAndFilterCourses(keyword, status);


		}
		else {


		    courses =
		        courseService.getAllCoursesForAdmin();


		}

		model.addAttribute(
		        "courses",
		        courses
		);
		
		model.addAttribute("keyword", keyword);

		model.addAttribute("status", status);
		
	    model.addAttribute(
	            "totalCourses",
	            courseService.countCourses()
	    );
	    
	    //for total courses
	    AdminDashboardDTO dashboard =
	            adminDashboardService.getDashboardData();



	    model.addAttribute(
	            "dashboard",
	            dashboard
	    );
	    
	    model.addAttribute(
	            "archivedCourses",
	            courseService.getArchivedCourses()
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
