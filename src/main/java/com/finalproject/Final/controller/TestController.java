package com.finalproject.Final.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.finalproject.Final.model.EnrollmentBean;
import com.finalproject.Final.model.UserBean;
import com.finalproject.Final.service.EnrollmentService;

import jakarta.servlet.http.HttpSession;

@Controller
public class TestController {

    @Autowired
    private EnrollmentService enrollmentService;


    @GetMapping("/test/enrollments")
    public String testEnrollments(HttpSession session) {


        UserBean student =
                (UserBean) session.getAttribute("loginUser");


        System.out.println("Logged User ID: "
                + student.getUserID());


        List<EnrollmentBean> list =
                enrollmentService.getByUser(
                        student.getUserID()
                );


        for(EnrollmentBean e : list) {

            System.out.println(
                    "Course: "
                    + e.getCourseTitle()
            );


            System.out.println(
                    "Teacher: "
                    + e.getTeacherName()
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

            System.out.println("--------------------");
        }


        return "test";
    }

}