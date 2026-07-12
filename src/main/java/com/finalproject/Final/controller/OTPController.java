package com.finalproject.Final.controller;
	import org.springframework.stereotype.Controller;
	import org.springframework.ui.Model;
	import org.springframework.web.bind.annotation.*;
	import jakarta.servlet.http.HttpSession;
	@Controller
	public class OTPController {

	    @GetMapping("/verify-otp")
	    public String verifyOtpPage() {
	        return "verify-otp";
	    }

	    @PostMapping("/verify-otp")
	    public String verifyOtp(@RequestParam String otp,
	                            HttpSession session,
	                            Model model) {

	        String sessionOtp = (String) session.getAttribute("otp");

	        if (sessionOtp == null) {
	            model.addAttribute("error", "OTP has expired.");
	            return "verify-otp";
	        }

	        if (sessionOtp.equals(otp)) {
	            return "redirect:/reset-password";
	        }

	        model.addAttribute("error", "Invalid OTP.");
	        return "verify-otp";
	    }
	}

