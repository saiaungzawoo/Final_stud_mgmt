package com.finalproject.Final.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.finalproject.Final.model.SubmissionBean;
import com.finalproject.Final.repository.StudentSubmissionRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/student/submission")
public class SubmissionController {
 @Autowired
    private StudentSubmissionRepository ssubmRepo;
 private final String UPLOAD_DIR = "D:/upload/assignment/";


    // Student Assignment List
   
    @GetMapping("/assignments")
    public String assignmentList(HttpSession session,
            Model model) {

String userID =(String) session.getAttribute("userID");
if(userID == null){
return "redirect:/login";
        }
model.addAttribute("assignments",ssubmRepo.getStudentAssignments(userID)
        );
return "student/assignment-list";
    }

// Open Submit Page
    
    @GetMapping("/assignment/submit/{id}")
    public String submitForm(
            @PathVariable("id") String assignmentID,
            HttpSession session,
            Model model) {
String userID =(String) session.getAttribute("userID");
if(userID == null){
            return "redirect:/login";
        }
// already submitted check
        if(ssubmRepo.hasSubmitted(
                assignmentID,
                userID)){
return "redirect:/student/submission/assignments";
        }
SubmissionBean bean =
                new SubmissionBean();
bean.setAssignmentID(assignmentID
        );
bean.setUserID(userID
        );
model.addAttribute(
                "submission",bean
        );
return "student/submit-assignment";
    }

// Save Submission
   
    @PostMapping("/assignment/submit")
    public String submitAssignment(
            @Valid @ModelAttribute("submission") SubmissionBean bean,
            HttpSession session,
            Model model) throws IOException {

String userID =(String) session.getAttribute("userID");
if(userID == null){
            return "redirect:/login";
        }

bean.setUserID(userID);

// duplicate check
        if(ssubmRepo.hasSubmitted(
                bean.getAssignmentID(),
                userID)) {
model.addAttribute(
                    "error",
                    "You have already submitted this assignment."
            );
return "student/submit-assignment";
        }
 MultipartFile file =
                bean.getFile();
// file required
        if(file == null || file.isEmpty()){
model.addAttribute(
                    "fileError",
                    "Please upload your assignment file."
            );
 return "student/submit-assignment";
        }

 // create folder
        File dir = new File(UPLOAD_DIR);
 if(!dir.exists()){
            dir.mkdirs();
        }
 // save file
        String fileName =
                UUID.randomUUID()
                + "_"
                + file.getOriginalFilename();
 File dest =new File(dir,fileName);
file.transferTo(dest);
 bean.setFilePath(
                "/upload/assignment/" + fileName
        );
 // save database
        ssubmRepo.submitAssignment(bean);
return "redirect:/student/submission/assignments";
    }

// My Submission
    
    @GetMapping("/my-submissions")
    public String mySubmission(
            HttpSession session,
            Model model) {
 String userID =
                (String) session.getAttribute("userID");
if(userID == null){
            return "redirect:/login";
        }
model.addAttribute(
                "submissions",
                ssubmRepo.getStudentSubmission(userID)
        );
return "student/my-submission";
    }

}