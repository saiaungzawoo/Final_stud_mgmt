package com.finalproject.Final.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.finalproject.Final.model.AdminEnrollmentDetailBean;
import com.finalproject.Final.model.EnrollmentBean;
import com.finalproject.Final.model.EnrollmentStatisticsBean;
import com.finalproject.Final.service.AdminEnrollmentDetailService;
import com.finalproject.Final.service.EnrollmentService;



@Controller
@RequestMapping("/admin/enrollment")
public class AdminEnrollmentController {


    @Autowired
    private EnrollmentService enrollmentService;
    
    @Autowired
    private AdminEnrollmentDetailService detailService;



    @GetMapping
    public String enrollmentManagement(Model model){



        List<EnrollmentBean> enrollments =
                enrollmentService.getAllEnrollments();
        
        
        EnrollmentStatisticsBean stats =
                enrollmentService.getEnrollmentStatistics();



        model.addAttribute(
                "enrollments",
                enrollments
        );
        
        model.addAttribute(
                "stats",
                stats
        );



        return "admin/admin-enrollment";

    }

    
    @GetMapping("/detail/{id}")
    public String enrollmentDetail(
            @PathVariable String id,
            Model model
    ){


        AdminEnrollmentDetailBean detail =
                detailService.getDetail(id);



        model.addAttribute(
                "detail",
                detail
        );



        return "admin/admin-enrollment-detail";

    }
    
    
    @PostMapping("/update-status")
    public String updateStatus(

            @RequestParam String enrollmentId,

            @RequestParam String status, 
            @RequestParam(required=false) String reason

    ){

        enrollmentService.updateEnrollmentStatus(
                enrollmentId,
                status, 
                reason
        );


        return "redirect:/admin/enrollment/detail/"
                + enrollmentId;

    }

}