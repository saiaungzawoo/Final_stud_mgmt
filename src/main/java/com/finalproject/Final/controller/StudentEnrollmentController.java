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
        


        	    




        return "student/my-enroll";

    }

}