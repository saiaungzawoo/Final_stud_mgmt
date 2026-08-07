package com.finalproject.Final.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finalproject.Final.model.EnrollmentBean;
import com.finalproject.Final.model.UserBean;
import com.finalproject.Final.service.EnrollmentService;

import jakarta.servlet.http.HttpSession;

@Controller
public class TestController {


    @Autowired
    private EnrollmentService enrollmentService;


    @GetMapping("/test-enrollment")
    @ResponseBody
    public String testEnrollment() {


//    	userId = "user001"
//    			courseId = "abc123"
        String enrollmentId =
                enrollmentService.createEnrollment(
                        "f74ca12b-44ea-4a00-a2c7-0e8a9215e96e",
                        "d37b4c45-a288-443e-aaca-ec4235340df4"
                );


        return "Created: " + enrollmentId;
    }

}

