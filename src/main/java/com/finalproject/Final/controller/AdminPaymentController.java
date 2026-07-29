package com.finalproject.Final.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String paymentManagement(Model model){


        List<PaymentBean> payments =
                paymentService.getAllPayments();
        
        PaymentStatisticsBean stats =
                paymentService.getPaymentStatistics();
        
        //test
//        System.out.println(payments.size());
//
//        for(PaymentBean p : payments){
//
//            System.out.println(
//                p.getStudentName()
//                +" | "
//                +p.getCourseName()
//                +" | "
//                +p.getAmount()
//            );
//
//        }
        
        
        System.out.println(
        	    "Collected: "
        	    + stats.getCollectedAmount()
        	);


        	System.out.println(
        	    "Outstanding: "
        	    + stats.getOutstandingAmount()
        	);


        	System.out.println(
        	    "Pending: "
        	    + stats.getPendingPayment()
        	);


        model.addAttribute(
                "payments",
                payments
        );
        
        model.addAttribute(
                "stats",
                stats
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