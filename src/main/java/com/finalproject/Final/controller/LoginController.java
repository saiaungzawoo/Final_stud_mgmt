package com.finalproject.Final.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.finalproject.Final.model.UserBean;
import com.finalproject.Final.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    UserRepository uRepo;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        HttpSession session,
                        Model m) {

        UserBean user = uRepo.findByEmail(email);
        
        //sai
        //role based login
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {

            session.setAttribute("loginUser", user);

            session.setAttribute("userID", user.getUserID());


            String role = user.getRoleName();
            
            //test
            System.out.println("ROLE = " + role);


            if("Admin".equals(role)) {

                return "redirect:/admin/dashboard";

            } else if("Teacher".equals(role)) {

                return "redirect:/dashboard/dashboard-teacher";

            } else {

                return "redirect:/";

            }

        }
        
       


//        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
//        	
//        	//test
////        	System.out.println("USER: " + user.getName());
////        	System.out.println("ROLE: " + user.getRoleName());
//
//            session.setAttribute("loginUser", user);
//            
//            session.setAttribute("userID", user.getUserID());//use for scholarship
//            return "redirect:/home";
        
       
  m.addAttribute("error", "Invalid email or password");
        return "auth/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}