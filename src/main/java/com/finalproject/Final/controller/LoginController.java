package com.finalproject.Final.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.finalproject.Final.model.UserBean;
import com.finalproject.Final.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UserRepository uRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    
     /* Login Page
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    
    /* Login Process
     */
    @PostMapping("/login")
    public String processLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {
        UserBean user = uRepo.findByEmail(email);

        System.out.println(user);

        if (user != null) {
            System.out.println(user.getEmail());
            System.out.println(user.getPassword());
            System.out.println(passwordEncoder.matches(password, user.getPassword()));
        }

        // Save login session
        session.setAttribute("loginUser", user);
        session.setAttribute("userID", user.getUserID());

        // Session timeout 30 minutes
        session.setMaxInactiveInterval(30 * 60);

        return "redirect:/home";
    }

    /**
     * Logout
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}