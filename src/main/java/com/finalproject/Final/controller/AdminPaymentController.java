package com.finalproject.Final.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.finalproject.Final.model.PaymentBean;
import com.finalproject.Final.model.PaymentHistoryBean;
import com.finalproject.Final.model.PaymentStatisticsBean;
import com.finalproject.Final.service.PaymentService;




@Controller
@RequestMapping("/admin/payment")
public class AdminPaymentController {


    @Autowired
    private PaymentService paymentService;



    @GetMapping
    public String paymentManagement(
    		@RequestParam(required = false) String keyword,
            @RequestParam(required = false) String paymentType,
    		
    		Model model){
    	
    	


//        List<PaymentBean> payments =
//                paymentService.getAllPayments();
        
        List<PaymentBean> payments;



        if(
            (keyword == null || keyword.isBlank())
            &&
            (paymentType == null || paymentType.isBlank())
        ){

            payments =
                paymentService.getAllPayments();

        }
        else{

            payments =
                paymentService.searchPayments(
                        keyword,
                        paymentType
                );

        }
        
        PaymentStatisticsBean stats =
                paymentService.getPaymentStatistics();
        
        
        
        


        model.addAttribute(
                "payments",
                payments
        );
        
        model.addAttribute(
                "stats",
                stats
        );
        
        model.addAttribute(
                "keyword",
                keyword
        );


        model.addAttribute(
                "paymentType",
                paymentType
        );




        return "admin/admin-payment";

    }
    
    
    @GetMapping("/detail/{paymentId}")
    public String paymentDetail(
            @PathVariable String paymentId,
            Model model
    ) {

        PaymentBean payment =
                paymentService.getById(paymentId);
        
        
        if(payment == null){
            return "redirect:/admin/payment";
        }


        List<PaymentHistoryBean> history =
                paymentService.getPaymentHistory(
                        payment.getEnrollmentId()
                );
        
       


        model.addAttribute(
                "paymentHistory",
                history
        );

        model.addAttribute(
                "payment",
                payment
        );

        return "admin/admin-payment-detail";

    }

}