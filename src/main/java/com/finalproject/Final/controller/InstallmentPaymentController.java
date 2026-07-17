package com.finalproject.Final.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.finalproject.Final.dto.InstallmentPaymentDTO;
import com.finalproject.Final.model.EnrollmentBean;
import com.finalproject.Final.model.InstallmentPlanBean;
import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.service.CourseService;
import com.finalproject.Final.service.EnrollmentService;
import com.finalproject.Final.service.InstallmentPlanService;
import com.finalproject.Final.service.PaymentMethodService;
import com.finalproject.Final.service.PaymentService;

@Controller
@RequestMapping("/payment")
public class InstallmentPaymentController {


    @Autowired
    private InstallmentPlanService installmentPlanService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private PaymentService paymentService;



    // SHOW INSTALLMENT PAYMENT PAGE
    @GetMapping("/installment/{installmentPlanId}")
    public String installmentPaymentPage(
            @PathVariable String installmentPlanId,
            Model model
    ){


        InstallmentPlanBean plan =
                installmentPlanService.getById(
                        installmentPlanId
                );


        EnrollmentBean enrollment =
                enrollmentService.getById(
                        plan.getEnrollmentId()
                );


        CourseBean course =
                courseService.getById(
                        enrollment.getCourseId()
                );


        model.addAttribute(
                "plan",
                plan
        );


        model.addAttribute(
                "enrollment",
                enrollment
        );


        model.addAttribute(
                "course",
                course
        );


        model.addAttribute(
                "paymentMethods",
                paymentMethodService.getAllActive()
        );


        return "student/installment-payment";
    }



    // PROCESS INSTALLMENT PAYMENT
    @PostMapping("/installment/pay")
    public String payInstallment(
            InstallmentPaymentDTO dto
    ){


        String paymentId =
                paymentService.processInstallmentPayment(
                        dto
                );


        return "redirect:/payment/success/"
                + paymentId;

    }

}