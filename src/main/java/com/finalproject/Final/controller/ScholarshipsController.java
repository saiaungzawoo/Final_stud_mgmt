package com.finalproject.Final.controller;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.finalproject.Final.model.ScholarshipApplicationBean;
import com.finalproject.Final.model.ScholarshipBean;
import com.finalproject.Final.model.UserBean;
import com.finalproject.Final.repository.ScholarshipsRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/scholarship")
public class ScholarshipsController {

	
	@Autowired
	ScholarshipsRepository schRepo;
	
	
	//show all scholarship for users
	@GetMapping("/scholarships")
	public String scholarshipList(Model model) {

	    List<ScholarshipBean> list = schRepo.getActiveScholarships();
	    model.addAttribute("scholarshipList", list);

	    return "student/scholarship-list";
	}
	
	
	// Scholarship detail view
	@GetMapping("/{id}")
	public String scholarshipDetail(
	        @PathVariable("id") String id,
	        HttpSession session,
	        Model model) {
ScholarshipBean scholarship = schRepo.findById(id);
model.addAttribute("scholarship", scholarship);
	    
//using for all 
	    //model.addAttribute("scholarship",scholarship);
//use for test these two lines
UserBean loginUser =
(UserBean) session.getAttribute("loginUser");


if(loginUser == null){
return "redirect:/login";
}

boolean applied =schRepo.hasApplied(id, loginUser.getUserID());
//CRITICAL FIX: Send the 'applied' boolean variable to your Thymeleaf template
model.addAttribute("applied", applied);
	    // Login ဝင်ထားတဲ့ userID ကို HTML ပို့
	   model.addAttribute("userID", session.getAttribute("userID"));
     return "student/scholarship-detail";
	}
	
	
	      

	   


 
 

 
// Apply button click
	@GetMapping("/apply/{id}")
	public String applyForm(
	        @PathVariable("id") String scholarshipID,
	        HttpSession session,
	        Model model) {


	    // Check Login
	    UserBean loginUser = 
	            (UserBean) session.getAttribute("loginUser");


	    if(loginUser == null){
	        return "redirect:/login";
	    }

	    String userID = loginUser.getUserID();


	    // Check Already Applied
	    if (schRepo.hasApplied(scholarshipID, userID)) {
   return "redirect:/scholarship/" + scholarshipID;
	    }

	    // Create Application Object
	    ScholarshipApplicationBean obj =
	            new ScholarshipApplicationBean();

	    obj.setScholarshipID(scholarshipID);
	    obj.setUserID(userID);
 model.addAttribute("application", obj);
 return "student/apply-scholarship";
	}

 // Submit Form
        @PostMapping("/save")
    public String saveApplication(@Valid @ModelAttribute("application")
            ScholarshipApplicationBean obj,
            BindingResult result,
            HttpSession session,
            Model model) throws IOException {

    	String userID =(String)session.getAttribute("userID");
        if (userID == null) {
            return "redirect:/login";
        }
        // get login user
        obj.setUserID(userID);
     // current date
        obj.setApplicationDate(LocalDate.now());
        
        
        
     // File upload
        MultipartFile file = obj.getFile();

        // File Validation
        if (file == null || file.isEmpty()) {
            result.rejectValue("file", "error.file",
                    "Supporting document is required.");
        } else {

            // Maximum 5 MB
            long maxSize = 5 * 1024 * 1024;

            if (file.getSize() > maxSize) {
                result.rejectValue("file", "error.file",
                        "File size must not exceed 5 MB.");
            }

            String originalFilename = file.getOriginalFilename();

            if (originalFilename == null || originalFilename.isBlank()) {
                result.rejectValue("file", "error.file",
                        "Invalid file.");
            } else {

                String lowerFilename = originalFilename.toLowerCase();

                if (!(lowerFilename.endsWith(".pdf")
                        || lowerFilename.endsWith(".doc")
                        || lowerFilename.endsWith(".docx")
                        || lowerFilename.endsWith(".ppt")
                        || lowerFilename.endsWith(".pptx"))) {

                    result.rejectValue("file", "error.file",
                            "Only PDF, DOC, DOCX, PPT and PPTX files are allowed.");
                }
            }
        }

        // Return form if validation fails
        if (result.hasErrors()) {
            return "student/apply-scholarship";
        }

        // Save file
        if (file != null && !file.isEmpty()) {

            String originalFilename = file.getOriginalFilename();

            // Generate unique and safe filename
            String fileName = UUID.randomUUID() + "_"
                    + originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");

            // Upload folder
            String uploadDir = "D:/upload/";
            Path uploadPath = Paths.get(uploadDir);

            // Create folder if it doesn't exist
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Save file (replace if exists)
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            // Save URL to database
            obj.setSupportingDocuments("/upload/" + fileName);
        }

        // Save application
        schRepo.insert(obj);

        return "redirect:/scholarship/scholarships";
        
        }
        
        @GetMapping("/my-applications")
        public String myApplications(
                HttpSession session,
                Model model){

            if(session.getAttribute("userID")==null){
return "redirect:/login";
            }

            String userID =
                    (String)session.getAttribute("userID");

            model.addAttribute(
                    "applications",
                    schRepo.getMyApplications(userID));

            return "student/application-scholarship";
        }
 

}


	
	

