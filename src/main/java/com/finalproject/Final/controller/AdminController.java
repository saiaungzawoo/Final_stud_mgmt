package com.finalproject.Final.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.finalproject.Final.dto.AdminDashboardDTO;
import com.finalproject.Final.service.AdminDashboardService;


@Controller
@RequestMapping("/admin")
public class AdminController {



	@Autowired
    private AdminDashboardService adminDashboardService;



 

	
	
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

}