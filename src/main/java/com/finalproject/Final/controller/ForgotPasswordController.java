package com.finalproject.Final.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.finalproject.Final.service.ForgotPasswordService;


import jakarta.servlet.http.HttpSession;



@Controller
public class ForgotPasswordController {



    @Autowired
    private ForgotPasswordService forgotPasswordService;





    // ==================================
    // Forgot Password Page
    // ==================================

    @GetMapping("/forgot-password")
    public String forgotPage(){

        return "forgot-password";

    }







    // ==================================
    // Send OTP
    // ==================================

    @PostMapping("/forgot-password/send-otp")
    public String sendOtp(
            @RequestParam String email,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ){



        boolean sent =
                forgotPasswordService.sendOtp(email);




        if(!sent){


            redirectAttributes.addFlashAttribute(
                    "error",
                    "Email not found"
            );


            return "redirect:/forgot-password";


        }






        session.setAttribute(
                "email",
                email
        );



        session.setAttribute(
                "otpVerified",
                false
        );





        // 1 minute timer
        // Change to 5 * 60 * 1000 for 5 minutes

        long expireTime =
                System.currentTimeMillis()
                + (1 * 60 * 1000);




        session.setAttribute(
                "otpExpireTime",
                expireTime
        );




        return "redirect:/verify-otp";


    }









    // ==================================
    // Verify OTP Page
    // ==================================

    @GetMapping("/verify-otp")
    public String verifyPage(
            HttpSession session,
            Model model
    ){



        String email =
                (String)session.getAttribute(
                        "email"
                );




        if(email == null){

            return "redirect:/forgot-password";

        }




        model.addAttribute(
                "expireTime",
                session.getAttribute(
                        "otpExpireTime"
                )
        );



        return "verify-otp";


    }









    // ==================================
    // Verify OTP
    // ==================================

    @PostMapping("/verify-otp")
    public String verifyOtp(
            @RequestParam String otp,
            HttpSession session,
            Model model
    ){



        String email =
                (String)session.getAttribute(
                        "email"
                );



        Long expireTime =
                (Long)session.getAttribute(
                        "otpExpireTime"
                );





        // Timer expired

        if(expireTime != null &&
                System.currentTimeMillis()
                > expireTime){



            model.addAttribute(
                    "error",
                    "OTP expired. Please resend OTP."
            );



            model.addAttribute(
                    "expireTime",
                    expireTime
            );



            return "verify-otp";


        }







        // OTP format check

        if(!otp.matches("\\d{6}")){


            model.addAttribute(
                    "error",
                    "Invalid OTP Code"
            );


            model.addAttribute(
                    "expireTime",
                    expireTime
            );


            return "verify-otp";


        }






        boolean result =
                forgotPasswordService.verifyOtp(
                        email,
                        otp
                );






        if(result){
session.setAttribute(
                    "otpVerified",
                    true
            );



            return "redirect:/new-password";


        }








        // Wrong OTP
        // Timer will NOT reset


        model.addAttribute(
                "error",
                "Invalid OTP Code"
        );



        model.addAttribute(
                "expireTime",
                expireTime
        );



        return "verify-otp";


    }

    // ==================================
    // Resend OTP
    // ==================================

    @PostMapping("/forgot-password/resend")
    public String resendOtp(
            HttpSession session,
            RedirectAttributes redirectAttributes
    ){



        String email =
                (String)session.getAttribute(
                        "email"
                );





        if(email == null){

            return "redirect:/forgot-password";

        }






        boolean sent =
                forgotPasswordService.sendOtp(email);





        if(sent){



            long expireTime =
                    System.currentTimeMillis()
                    + (1 * 60 * 1000);




            session.setAttribute(
                    "otpExpireTime",
                    expireTime
            );




            redirectAttributes.addFlashAttribute(
                    "message",
                    "New OTP sent"
            );


        }


        return "redirect:/verify-otp";


    }
    // ==================================
    // New Password Page
    // ==================================

    @GetMapping("/new-password")
    public String newPasswordPage(
            HttpSession session
    ){



        Boolean verified =
                (Boolean)session.getAttribute(
                        "otpVerified"
                );



        if(verified == null || !verified){

            return "redirect:/forgot-password";

        }




        return "new-password";


    }
    
    
    
    // ==================================
    // Update Password
    // ==================================

    @PostMapping("/new-password")
    public String updatePassword(
            @RequestParam String newPassword,
            HttpSession session
    ){

        String email =
                (String) session.getAttribute("email");


        if(email != null){

            forgotPasswordService.updatePassword(
                    email,
                    newPassword
            );

        }


        session.invalidate();


        return "redirect:/login";
    }



}