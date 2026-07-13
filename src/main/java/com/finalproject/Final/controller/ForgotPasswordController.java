package com.finalproject.Final.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.finalproject.Final.service.ForgotPasswordService;

@Controller
public class ForgotPasswordController {

	//test
    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password"; 
    }

    // Email လက်ခံပြီး OTP လှမ်းပိုပေးသည့် Endpoint
    @PostMapping("/forgot-password/send-otp")
    public String sendOtp(@RequestParam String email, Model m) {
        boolean isSent = forgotPasswordService.sendOtp(email);
        
        if (isSent) {
            m.addAttribute("email", email);
            m.addAttribute("showOtpForm", true); // ဒုတိယအဆင့် OTP ဖြည့်သည့် Form ကို ပြရန်
            m.addAttribute("message", "OTP sent successfully to your email.");
        } else {
            m.addAttribute("error", "Email not found or failed to send OTP.");
        }
        return "forgot-password";
    }

    // OTP နှင့် စကားဝှက်အသစ်ကို စစ်ဆေးအတည်ပြုပေးသည့် Endpoint
    @PostMapping("/forgot-password/reset")
    public String resetPassword(@RequestParam String email,
                                 @RequestParam String otp,
                                 @RequestParam String newPassword,
                                 Model m) {

        String result = forgotPasswordService.verifyAndResetPassword(email, otp, newPassword);

        if ("SUCCESS".equals(result)) {
            m.addAttribute("message", "Password updated successfully. Please login.");
            return "login"; // Password ပြောင်းလဲပြီးပါက Login Page သို တန်းပိုပေးမည်
        } else {
            m.addAttribute("email", email);
            m.addAttribute("showOtpForm", true);
            m.addAttribute("error", result);
            return "forgot-password";
        }
    }
}