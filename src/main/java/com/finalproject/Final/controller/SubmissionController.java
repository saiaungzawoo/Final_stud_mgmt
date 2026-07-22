package com.finalproject.Final.controller;

import java.io.File;
import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import com.finalproject.Final.model.SubmissionBean;
import com.finalproject.Final.repository.StudentSubmissionRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/submission")
public class SubmissionController {

	@Autowired
	private StudentSubmissionRepository ssubmRepo;
private final String UPLOAD_DIR = "D:/upload/assignment/";

  
//    // Assignment List view all assignments  using for test
//   @GetMapping("/assignments")
//    public String assignmentList(Model model) {
//
//        model.addAttribute("assignments",
//        		ssubmRepo.getAllAssignment());
//return "student/assignment-list";
//   }


//view when he enrolled the course assignment
   @GetMapping("/assignments")
  public String assignmentList(HttpSession session, Model model) {

       String userID = (String) session.getAttribute("userID");

     if (userID == null) {
           return "redirect:/login";
       }

      model.addAttribute("assignments",
               ssubmRepo.getStudentAssignments(userID));
  return "student/assignment-list";
  }
 
 //Assignment Submit Form
  @GetMapping("/assignment/submit/{id}")
    public String submitForm(@PathVariable("id") String assignmentID,
                             Model model) {
 SubmissionBean bean = new SubmissionBean();
        bean.setAssignmentID(assignmentID);
model.addAttribute("submission", bean);
return "student/submit-assignment";
    }

  
  
  
    // Save Submission
  @PostMapping("/assignment/submit")
  public String submitAssignment(
          @Valid @ModelAttribute("submission") SubmissionBean bean,
          BindingResult result,
          HttpSession session,
          Model model) throws IOException {

      String userID = (String) session.getAttribute("userID");

      if (userID == null) {
          return "redirect:/login";
      }

      // Bean validation
      if (result.hasErrors()) {
          return "student/submit-assignment";
      }

      bean.setUserID(userID);

      // Check duplicate submission
      if (ssubmRepo.hasSubmitted(bean.getAssignmentID(), userID)) {
          model.addAttribute("error", "You have already submitted this assignment.");
          return "student/submit-assignment";
      }

      MultipartFile file = bean.getFile();
      
//်file validation
      if (file == null || file.isEmpty()) {
    	  model.addAttribute("fileError", "Please upload your assignment file.");

    	    return "student/submit-assignment";
    	}
      
//      // File validation
//      if (file == null || file.isEmpty()) {
//          model.addAttribute("error", "Please upload your assignment file.");
//          return "student/submit-assignment";
//      }

      // Save uploaded file
      String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
File dir = new File(UPLOAD_DIR);
      if (!dir.exists()) {
          dir.mkdirs();
      }

      File dest = new File(dir, fileName);
      file.transferTo(dest);
bean.setFilePath("/upload/assignment/" + fileName);

      // Save submission to database
      ssubmRepo.submitAssignment(bean);
return "redirect:/submission/my-submissions";
  }
 
    // My Submission List view
 @GetMapping("/my-submissions")
    public String mySubmission(HttpSession session,
                               Model model) {
String userID = (String) session.getAttribute("userID");
 if (userID == null) {
            return "redirect:/login";
        }
model.addAttribute("submissions",
                ssubmRepo.getStudentSubmission(userID));

        return "student/my-submission";
    }

}


