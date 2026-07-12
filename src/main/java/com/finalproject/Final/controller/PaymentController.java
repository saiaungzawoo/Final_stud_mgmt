package com.finalproject.Final.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finalproject.Final.dto.PaymentDTO;
import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.model.EnrollmentBean;
import com.finalproject.Final.model.InstallmentRuleBean;
import com.finalproject.Final.model.InstallmentRuleItemBean;
import com.finalproject.Final.model.PaymentBean;
import com.finalproject.Final.repository.CourseRepository;
import com.finalproject.Final.service.CourseService;
import com.finalproject.Final.service.EnrollmentService;
import com.finalproject.Final.service.InstallmentRuleItemService;
import com.finalproject.Final.service.InstallmentRuleService;
import com.finalproject.Final.service.PaymentMethodService;
import com.finalproject.Final.service.PaymentService;
import com.finalproject.Final.service.PaymentTypeService;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    
    @Autowired
    private PaymentTypeService paymentTypeService;

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private EnrollmentService enrollmentService;
    
    @Autowired
    private InstallmentRuleService installmentRuleService;


    @Autowired
    private InstallmentRuleItemService installmentRuleItemService;

    // show payment page
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private CourseRepository courseRepo;

//    @GetMapping("/page/{enrollmentId}")
//    public String paymentPage(@PathVariable String enrollmentId, Model model) {
//
//        try {
//            EnrollmentBean enrollment = enrollmentService.getById(enrollmentId);
//            CourseBean course = courseService.getById(enrollment.getCourseId());
//
//            model.addAttribute("enrollment", enrollment);
//            model.addAttribute("course", course);
//           
//
//            return "student/payment";
//
//        } catch (RuntimeException e) {
//
//            model.addAttribute("errorMessage", e.getMessage());
//            return "student/payment";
//        }
//    }
    
    @GetMapping("/page/{id}")
    public String paymentPage(
            @PathVariable String id,
            Model model
    ){

        EnrollmentBean enrollment =
            enrollmentService.getById(id);


        CourseBean course =
            courseRepo.findById(
                enrollment.getCourseId()
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
        	    "paymentTypes",
        	    paymentTypeService.getAll()
        	);

        	model.addAttribute(
        	    "paymentMethods",
        	    paymentMethodService.getAllActive()
        	);
        	
        	 // Load installment information

            InstallmentRuleBean installmentRule =
                    installmentRuleService.getByCourseId(
                            course.getCourseId()
                    );


            model.addAttribute(
                    "installmentRule",
                    installmentRule
            );



            if(installmentRule != null) {


                List<InstallmentRuleItemBean> installmentItems =
                        installmentRuleItemService.getByRuleId(
                                installmentRule.getInstallmentRuleId()
                        );


                model.addAttribute(
                        "installmentItems",
                        installmentItems
                );

            }


        return "student/payment";
    }

    // process payment
//    @PostMapping("/pay")
//    public String pay(PaymentDTO dto) {
//
//        paymentService.processPayment(dto);
//
//        return "redirect:/enrollment/my?userId=" + dto.getUserId();
//    }
    
//    @GetMapping("/success/{enrollmentId}")
//    public String enrollSuccess(@PathVariable String enrollmentId,
//                                Model model) {
//
//        EnrollmentBean enrollment = enrollmentService.getById(enrollmentId);
//        CourseBean course = courseService.getById(enrollment.getCourseId());
//
//        PaymentBean payment = paymentService.getByEnrollmentId(enrollmentId);
//
//        model.addAttribute("enrollment", enrollment);
//        model.addAttribute("course", course);
//        model.addAttribute("payment", payment);
//        model.addAttribute("paymentStatus", "PAID");
//
//        return "student/enroll-success";
//    }
    @GetMapping("/success/{id}")
    public String success(
            @PathVariable String id,
            Model model) {

        PaymentBean payment = paymentService.getById(id);

        EnrollmentBean enrollment =
                enrollmentService.getById(
                        payment.getEnrollmentId());

        CourseBean course =
                courseService.getById(
                        enrollment.getCourseId());

        model.addAttribute("payment", payment);
        model.addAttribute("enrollment", enrollment);
        model.addAttribute("course", course);

        return "student/enroll-success";
    }
    
//    @PostMapping("/pay")
//    public String pay(PaymentDTO dto) {
//
//        paymentService.processPayment(dto);
//
//        return "redirect:/payment/success/" + dto.getEnrollmentId();
//    }
    
    @PostMapping("/pay")
    public String pay(
            PaymentDTO dto,
            RedirectAttributes redirect
    ){

    String paymentId =
    paymentService.processPayment(dto);


    return "redirect:/payment/success/"+paymentId;

    }
}

