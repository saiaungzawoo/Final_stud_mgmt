package com.finalproject.Final.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.finalproject.Final.service.ForgotPasswordService;
import jakarta.servlet.http.HttpSession;
@Controller
public class ForgotPasswordController {
    @Autowired
    private ForgotPasswordService forgotPasswordService;
    @GetMapping("/forgot-password")
    public String forgotPage() {

        return "forgot-password";
    }




    // Send OTP
    @PostMapping("/forgot-password/send-otp")
    public String sendOtp(
            @RequestParam String email,
            HttpSession session,
            Model model
    ) {


        boolean sent =
                forgotPasswordService.sendOtp(email);



        if(sent) {


            session.setAttribute(
                    "email",
                    email
            );


            return "redirect:/verify-otp";


        } else {


            model.addAttribute(
                    "error",
                    "Email not found"
            );


            return "forgot-password";

        }

    }
    // Resend OTP
    @PostMapping("/forgot-password/resend")
    public String resendOtp(
            @RequestParam String email,
            HttpSession session,
            Model model
    ) {


        boolean sent =
                forgotPasswordService.sendOtp(email);



        if(sent) {


            session.setAttribute(
                    "email",
                    email
            );


            return "redirect:/verify-otp";


        } else {


            model.addAttribute(
                    "error",
                    "Email not found"
            );


            return "verify-otp";

        }

    }





    // OTP Page
    @GetMapping("/verify-otp")
    public String otpPage() {

        return "verify-otp";

    }




    // Verify OTP
    @PostMapping("/verify-otp")
    public String verifyOtp(
            @RequestParam String otp,
            HttpSession session,
            Model model
    ) {


        String email =
                (String)session.getAttribute("email");



        String result =
                forgotPasswordService.verifyOtp(
                        email,
                        otp
                );



        if("SUCCESS".equals(result)) {


            return "redirect:/new-password";


        } else {


            model.addAttribute(
                    "error",
                    result
            );


            return "verify-otp";

        }

    }




    // New Password Page
    @GetMapping("/new-password")
    public String newPasswordPage() {

        return "new-password";

    }




    // Update Password
    @PostMapping("/new-password")
    public String updatePassword(
            @RequestParam String newPassword,
            HttpSession session
    ) {


        String email =
                (String)session.getAttribute("email");



        forgotPasswordService.updatePassword(
                email,
                newPassword
        );



        session.invalidate();



        return "redirect:/login";

    }

}