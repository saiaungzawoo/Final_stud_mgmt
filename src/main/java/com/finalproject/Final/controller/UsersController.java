package com.finalproject.Final.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.imageio.ImageIO;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.finalproject.Final.model.UserBean;
import com.finalproject.Final.repository.UsersRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/student")
public class UsersController {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository uRepo;

    @GetMapping("/register")
    public String registerPage(Model m) {
        UserBean user = new UserBean();
        user.setGender("Male");
        m.addAttribute("userObj", user);
        return "student/student_register";
    }

    @GetMapping("/registers")
    public String success(Model m) {
        UserBean user = uRepo.getLatestStudent();
        m.addAttribute("userObj", user);
        return "student/success";
    }

    @PostMapping("/register")
    public String studentRegistrater(
            @Valid @ModelAttribute("userObj") UserBean obj,
            BindingResult br,
            Model m,
            @RequestParam("photo") MultipartFile photo) throws IOException {

        if (obj.getDob() != null
                && obj.getDob().isAfter(LocalDate.now().minusYears(16))) {
            br.rejectValue("dob", "error.dob", "Age must be at least 16 years old");
        }

        if (br.hasErrors()) {
            return "student/student_register";
        }

        if (photo.isEmpty()) {
            m.addAttribute("error", "Please select a photo");
            return "student/student_register";
        }

        long maxSize = 2 * 1024 * 1024;
        if (photo.getSize() > maxSize) {
            m.addAttribute("error", "Photo size must not exceed 2 MB!");
            return "student/student_register";
        }

        String contentType = photo.getContentType();
        if (contentType == null
                || !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
            m.addAttribute("error", "Invalid image file");
            return "student/student_register";
        }

        BufferedImage image = ImageIO.read(photo.getInputStream());
        if (image == null) {
            m.addAttribute("error", "Invalid image file");
            return "student/student_register";
        }

        String fileName = photo.getOriginalFilename();
        String path = "D:/upload/";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        photo.transferTo(new File(path + fileName));
        obj.setProfileImage("/upload/" + fileName);

        if (uRepo.existsByEmail(obj.getEmail())) {
            m.addAttribute("emailError", "Email already exists");
            return "student/student_register";
        }

        obj.setUserID(UUID.randomUUID().toString());
        obj.setRoleID("19dac244-7acd-11f1-898e-e4b97a5cf834");
        obj.setIsActive(1);
        obj.setCreatedAt(LocalDateTime.now());
        obj.setUpdatedAt(LocalDateTime.now());
        obj.setPassword(passwordEncoder.encode(obj.getPassword()));

        uRepo.insertUser(obj);
        m.addAttribute("userObj", obj);
        return "student/success";
    }

    @PostMapping("/update")
    public String updateStudent(
            @ModelAttribute("userObj") UserBean userObj,
            BindingResult br,
            Model model,
            @RequestParam("photo") MultipartFile photo) throws IOException {

        if (userObj.getDob() != null
                && userObj.getDob().isAfter(LocalDate.now().minusYears(16))) {
            br.rejectValue("dob", "error.dob", "Age must be at least 16 years old.");
        }

        if (userObj.getPassword() != null && !userObj.getPassword().isBlank()) {
            String password = userObj.getPassword();

            if (password.length() < 6) {
                br.rejectValue("password", "error.password",
                        "Password must be at least 6 characters long.");
            }
            if (!password.matches(".*[A-Za-z].*")) {
                br.rejectValue("password", "error.password",
                        "Password must contain at least one letter.");
            }
            if (!password.matches(".*\\d.*")) {
                br.rejectValue("password", "error.password",
                        "Password must contain at least one number.");
            }
        }

        if (br.hasErrors()) {
            return "student/student_edit";
        }

        UserBean oldUser = uRepo.getUserById(userObj.getUserID());
        if (oldUser == null) {
            model.addAttribute("error", "User not found.");
            return "student/student_edit";
        }

        if (uRepo.existsByEmailAndNotUserId(userObj.getEmail(), userObj.getUserID())) {
            model.addAttribute("emailError", "Email already exists.");
            return "student/student_edit";
        }

        if (!photo.isEmpty()) {
            long maxSize = 5 * 1024 * 1024;
            if (photo.getSize() > maxSize) {
                model.addAttribute("error", "Photo size must not exceed 5 MB.");
                return "student/student_edit";
            }

            String contentType = photo.getContentType();
            if (contentType == null
                    || !(contentType.equals("image/jpeg") || contentType.equals("image/png"))) {
                model.addAttribute("error", "Only JPG and PNG images are allowed.");
                return "student/student_edit";
            }

            BufferedImage image = ImageIO.read(photo.getInputStream());
            if (image == null) {
                model.addAttribute("error", "Invalid image.");
                return "student/student_edit";
            }

            String uploadPath = "D:/upload/";
            File dir = new File(uploadPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = photo.getOriginalFilename();
            photo.transferTo(new File(dir, fileName));
            userObj.setProfileImage("/upload/" + fileName);
        } else {
            userObj.setProfileImage(oldUser.getProfileImage());
        }

        userObj.setRoleID(oldUser.getRoleID());
        userObj.setIsActive(oldUser.getIsActive());
        userObj.setCreatedAt(oldUser.getCreatedAt());
        userObj.setUpdatedAt(LocalDateTime.now());

        if (userObj.getPassword() == null || userObj.getPassword().isBlank()) {
            userObj.setPassword(oldUser.getPassword());
        } else {
            userObj.setPassword(passwordEncoder.encode(userObj.getPassword()));
        }

        uRepo.updateUser(userObj);
        return "student/student-profile";
    }

    @GetMapping("/update")
    public String update(Model m) {
        UserBean user = uRepo.getLatestStudent();
        m.addAttribute("userObj", user);
        return "student/student_edit";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        UserBean loginUser = (UserBean) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        UserBean userObj = uRepo.getUserByEmail(loginUser.getEmail());
        model.addAttribute("userObj", userObj);
        return "student/student-profile";
    }
}

  
  

