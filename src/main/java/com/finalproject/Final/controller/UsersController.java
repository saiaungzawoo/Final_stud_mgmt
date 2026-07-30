 package com.finalproject.Final.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
        user.setGender("Male");   // Default selected
        m.addAttribute("userObj", user);
     
      return "student/student_register";
  }
  
  @GetMapping("/registers")
  public String success(Model m) {
     UserBean user = uRepo.getLatestStudent();
      m.addAttribute("userObj",user);
      
  
     return "student/success";
  }
  
  
  
  @PostMapping("/register")
  public String studentRegistrater(@Valid @ModelAttribute("userObj")UserBean obj,
      BindingResult br,Model m,
      @RequestParam("photo") MultipartFile photo) throws IOException {
    
    //age must be 16 vallidation
    if (obj.getDob() != null &&
            obj.getDob().isAfter(LocalDate.now().minusYears(16))) {
           br.rejectValue(
                    "dob",
                    "error.dob",
                    "Age must be at least 16 years old"); }
    
    if(br.hasErrors()) {
      //return"student/student_register";
      return"student/student_register";
    }
    
    if (photo.isEmpty()) {
          m.addAttribute("error", "Please select a photo");
          //return "student/student_register";
          return "student/student_register";
      }
    //photo size 
     long maxSize = 2 * 1024 * 1024;
       if (photo.getSize() > maxSize) {
            m.addAttribute("error",
                    "Photo size must not exceed 2 MB!");
            return"student/student_register";
           // return "student_register";
       }
            
             // Content Type Validation
          String contentType = photo.getContentType();

          if (contentType == null ||
                  !(contentType.equals("image/jpeg") ||
                           contentType.equals("image/png"))) {

              m.addAttribute("error", "Invalid image file");
              return "student/student_register";
            
          }
          
          BufferedImage image = ImageIO.read(photo.getInputStream());
        if (image == null) {
              m.addAttribute("error",
                      "Invalid image file");
              return "student/student_register";
            
              }
        String fileName = photo.getOriginalFilename();
 String path = "D:/upload/";
//file 
          File dir = new File(path);
          if (!dir.exists()) {
              dir.mkdirs();
          }
          //save file
          photo.transferTo(new File(path + fileName));
 //save file path
          obj.setProfileImage("/upload/" + fileName);
          
         
        if(uRepo.existsByEmail(obj.getEmail())) {
          m.addAttribute("emailError",
                  "Email already exists");
          return "student/student_register";
         
      }
        
     // Generate UUID for user
        obj.setUserID(UUID.randomUUID().toString());
     // Get Student Role ID from role table
        String studentRoleId = uRepo.getRoleIdByName("Student");
        obj.setRoleID(studentRoleId);

        // Student Role UUID (Replace with your actual Student role UUID)
       // obj.setRoleID("3c2f4396-7a84-11f1-bfcb-b4b686e7f920");

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

      // Age Validation
      if (userObj.getDob() != null &&
              userObj.getDob().isAfter(LocalDate.now().minusYears(16))) {

          br.rejectValue(
                  "dob",
                  "error.dob",
                  "Age must be at least 16 years old.");
      }
      // Password validation only if user enters a new password
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
        // return "student_edit";
      }

      // Get Existing User
      UserBean oldUser = uRepo.getUserById(userObj.getUserID());

      if (oldUser == null) {
          model.addAttribute("error", "User not found.");
         return "student/student_edit";
          //return "student_edit";
      }

      // Email Duplicate Check
      UserBean emailUser = uRepo.getUserByEmail(userObj.getEmail());
if (uRepo.existsByEmailAndNotUserId(
              userObj.getEmail(),
              userObj.getUserID())) {

          model.addAttribute("emailError", "Email already exists.");
          return "student/student_edit";
      }
     // if (emailUser != null &&
         //     emailUser.getUserId() != userObj.getUserId()) {

       //   model.addAttribute(
          //        "emailError",
            //      "Email already exists.");
         // return "student/student_edit";
       //   return "student_edit";
    //  }

      // Photo Upload (Optional)
      if (!photo.isEmpty()) {

          // Size Validation
          long maxSize = 5 * 1024 * 1024;

          if (photo.getSize() > maxSize) {

              model.addAttribute(
                      "error",
                      "Photo size must not exceed 5 MB.");

              return "student/student_edit";
             // return "student_edit";
          }

          // Content Type Validation
          String contentType = photo.getContentType();

          if (contentType == null ||
                 !(contentType.equals("image/jpeg") 
                         ||  contentType.equals("image/png"))) {
 model.addAttribute(
                      "error",
                      "Only JPG and PNG images are allowed.");

             return "student/student_edit";
             // return "student_edit";
          } // Check Image
          BufferedImage image =
                  ImageIO.read(photo.getInputStream());

          if (image == null) {

              model.addAttribute(
                      "error",
                      "Invalid image.");

              return "student/student_edit";
             // return "student_edit";
          }

          // Upload Folder
          String uploadPath = "D:/upload/";
   File dir = new File(uploadPath);
          if (!dir.exists()) {
              dir.mkdirs();
              }

         // Save File
          String fileName=photo.getOriginalFilename();//n
          photo.transferTo(new File(dir, fileName));

          userObj.setProfileImage("/upload/" + fileName);
          //System.out.println(userObj.getFilePath());
      } else {

          // Keep Old Photo
          userObj.setProfileImage(oldUser.getProfileImage());

      }

      // Keep existing values
      userObj.setRoleID(oldUser.getRoleID());
      userObj.setIsActive(oldUser.getIsActive());
      userObj.setCreatedAt(oldUser.getCreatedAt());
      userObj.setUpdatedAt(LocalDateTime.now());

   // Password
      if (userObj.getPassword() == null || userObj.getPassword().isBlank()) {
          userObj.setPassword(oldUser.getPassword());
      } else {
          userObj.setPassword(passwordEncoder.encode(userObj.getPassword()));
      }
      uRepo.updateUser(userObj);

     
      // Update User
      uRepo.updateUser(userObj);
      return "student/student-profile";
      //return "student-profile";
    
  }
  @GetMapping("/update")
  public String update(HttpSession session, Model model) {

      UserBean loginUser = (UserBean) session.getAttribute("loginUser");

      if (loginUser == null) {
          return "redirect:/login";
      }

      UserBean user = uRepo.getUserById(loginUser.getUserID());

      model.addAttribute("userObj", user);

      return "student/student_edit";
  }
//  @GetMapping("/update")
//  public String update(Model m) {
//     UserBean user = uRepo.getLatestStudent();
//      m.addAttribute("userObj", user);
//      
//      
//     // return "success";
//      return "student/student_edit";
//    //  return "student_edit";
//  }

  @GetMapping("/profile")
  public String profile(HttpSession session, Model model) {

      UserBean loginUser = (UserBean) session.getAttribute("loginUser");

      if (loginUser == null) {
          return "redirect:/login";
      }

      // Database ထဲက latest data ပြန်ယူချင်ရင်
      UserBean userObj = uRepo.getUserByEmail(loginUser.getEmail());
   model.addAttribute("userObj", userObj);

     return "student/student-profile";
      //return "student-profile";
  }
  
//use for admin
  @GetMapping("/admin/students")
  public String viewStudents(Model model) {

      List<UserBean> students = uRepo.selectAllStudents();

      model.addAttribute("students", students);
 return "admin/adminstudent-list";
  }
  
  //use for admin
  @GetMapping("/admin/student/detail/{id}")
  public String studentDetail(
          @PathVariable String id,
          Model model) {

      UserBean student = uRepo.selectStudentById(id);

      model.addAttribute("student", student);

      return "admin/adminstudent-detail";
  }
  
  
  //add
  //admin profile
  @GetMapping("/admin/profile")
  public String adminProfile(HttpSession session, Model model) {

      UserBean loginUser = (UserBean) session.getAttribute("loginUser");

      if (loginUser == null) {
          return "redirect:/login";
      }

      UserBean admin = uRepo.getUserById(loginUser.getUserID());

      model.addAttribute("adminObj", admin);

      return "admin/admin-profile";
  }
  //admin profile edit 
  @GetMapping("/admin/profile/edit")
  public String editAdminProfile(HttpSession session, Model model) {

      UserBean loginUser = (UserBean) session.getAttribute("loginUser");

      if (loginUser == null) {
          return "redirect:/login";
      }

      UserBean admin = uRepo.getUserById(loginUser.getUserID());

      model.addAttribute("adminObj", admin);

      return "admin/adminprofile-edit";
  }
  
  //admin profile update
  @PostMapping("/admin/profile/update")
  public String updateAdmin(
           @ModelAttribute("adminObj") UserBean adminObj,
          BindingResult result,
          @RequestParam("photo") MultipartFile photo,
          Model model,
          HttpSession session) throws IOException {

	// Password validation only if user enters a new password
      if (adminObj.getPassword() != null && !adminObj.getPassword().isBlank()) {
          String password = adminObj.getPassword();

          if (password.length() < 6) {
        	  result.rejectValue("password", "error.password",
                      "Password must be at least 6 characters long.");
          }

          if (!password.matches(".*[A-Za-z].*")) {
        	  result.rejectValue("password", "error.password",
                      "Password must contain at least one letter.");
          }

          if (!password.matches(".*\\d.*")) {
        	  result.rejectValue("password", "error.password",
                      "Password must contain at least one number.");
          }
      }
      if (result.hasErrors()) {
          return "admin/adminprofile-edit";
      }
 UserBean oldAdmin = uRepo.getUserById(adminObj.getUserID());

      if (oldAdmin == null) {
          model.addAttribute("error", "Admin not found.");
          return "admin/adminprofile-edit";
      }

      // Email duplicate
      if (uRepo.existsByEmailAndNotUserId(
              adminObj.getEmail(),
              adminObj.getUserID())) {

          model.addAttribute("emailError",
                  "Email already exists.");

          return "admin/adminprofile-edit";
      }

      // Upload image
      if (photo != null && !photo.isEmpty()) {

          long maxSize = 2 * 1024 * 1024;

          if (photo.getSize() > maxSize) {
              model.addAttribute("error",
                      "Photo must not exceed 2 MB.");
              return "admin/adminprofile-edit";
          }

          String type = photo.getContentType();

          if (type == null ||
                  !(type.equals("image/jpeg")
                  || type.equals("image/png"))) {

              model.addAttribute("error",
                      "Only JPG and PNG are allowed.");

              return "admin/adminprofile-edit";
          }

          String uploadDir = "D:/upload/";

          File dir = new File(uploadDir);

          if (!dir.exists()) {
              dir.mkdirs();
          }

          String fileName =
                  UUID.randomUUID() + "_" + photo.getOriginalFilename();

          photo.transferTo(new File(dir, fileName));

          adminObj.setProfileImage("/upload/" + fileName);

      } else {

          adminObj.setProfileImage(oldAdmin.getProfileImage());

      }

      // Password
      if (adminObj.getPassword() == null ||
              adminObj.getPassword().isBlank()) {

          adminObj.setPassword(oldAdmin.getPassword());

      } else {

          adminObj.setPassword(
                  passwordEncoder.encode(adminObj.getPassword()));
      }

      adminObj.setRoleID(oldAdmin.getRoleID());
      adminObj.setCreatedAt(oldAdmin.getCreatedAt());
      adminObj.setUpdatedAt(LocalDateTime.now());
      adminObj.setIsActive(oldAdmin.getIsActive());

      uRepo.updateUser(adminObj);

      session.setAttribute("loginUser", adminObj);

      return "redirect:/student/admin/profile";
  }
 
}