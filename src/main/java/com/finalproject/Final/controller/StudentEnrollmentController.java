package com.finalproject.Final.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.finalproject.Final.model.EnrollmentBean;
import com.finalproject.Final.model.UserBean;
import com.finalproject.Final.service.EnrollmentService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/student")
public class StudentEnrollmentController {


    @Autowired
    private EnrollmentService enrollmentService;



    @GetMapping("/my-enrollments")
    public String myEnrollments(
            HttpSession session,
            Model model
    ){


        UserBean student =
                (UserBean) session.getAttribute("loginUser");


        if(student == null){

            return "redirect:/login";

        }


        List<EnrollmentBean> enrollments =
                enrollmentService.getMyEnrollments(
                        student.getUserID()
                );


        model.addAttribute(
                "enrollments",
                enrollments
        );
        
        System.out.println(
        	    "COURSE COUNT: "
        	    + enrollments.size()
        	);


        	for(EnrollmentBean e : enrollments){

        	    System.out.println(
        	        e.getCourseTitle()
        	    );

        	    System.out.println(
        	        "Paid: "
        	        + e.getTotalPaid()
        	    );

        	    System.out.println(
        	        "Remaining: "
        	        + e.getRemainingBalance()
        	    );

        	    System.out.println(
        	        "Installments: "
        	        + e.getCompletedInstallments()
        	        + "/"
        	        + e.getTotalInstallments()
        	    );
        	    
        	    System.out.println("en date: " + e.getEnrollmentDate());
        	    
        	    for(EnrollmentBean e1 : enrollments){

        	        System.out.println(
        	            "COURSE: " + e1.getCourseTitle()
        	        );


        	        if(e1.getInstallmentPlans()!=null){

        	            for(var plan : e1.getInstallmentPlans()){

        	                System.out.println(
        	                    "Installment "
        	                    + plan.getInstallmentNumber()
        	                    + " Amount "
        	                    + plan.getAmountDue()
        	                    + " Status "
        	                    + plan.getStatus()
        	                );

        	            }

        	        }

        	    }

        	}


        return "student/my-enroll";

    }

}