package com.finalproject.Final.controller;


import java.time.LocalDate;
import java.time.LocalDateTime;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.model.ScholarshipApplicationBean;
import com.finalproject.Final.model.ScholarshipBean;
import com.finalproject.Final.repository.ScholarshipApplicationRepository;
import com.lowagie.text.List;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@Controller
@RequestMapping("/admin")
public class ScholarshipsControllerAdmin {

	@Autowired
    private ScholarshipApplicationRepository sArepo;


    // Create Scholarship Page
  
    @GetMapping("/scholarship-create")
    public String create(Model model){

    	model.addAttribute("scholarship", new ScholarshipBean());
        
        // MAKE SURE THIS LINE EXISTS
        model.addAttribute("courses", sArepo.getAllCourseNa()); 
        
        return "admin/create-scholarship";
    }
    @PostMapping("/save")
    public String save(
           @Valid @ModelAttribute("scholarship") ScholarshipBean scholarship,
           BindingResult result,
           Model model,
           HttpSession session) {

        // If validation error
        if (result.hasErrors()) {
            // FIX: Match the repository call used in the original create method
            model.addAttribute("courses", sArepo.getAllCourseNa()); 
 return "admin/create-scholarship";
        }

        scholarship.setScholarshipID(java.util.UUID.randomUUID().toString());
        scholarship.setCreatedAt(LocalDateTime.now());
        scholarship.setUpdatedAt(LocalDateTime.now());

        sArepo.insert(scholarship);

        return "redirect:/admin/list";
    }
	
    
  

    // Scholarship Management List
     
    @GetMapping("/list")
    public String list(Model model){

 model.addAttribute("scholarships",sArepo.getAll()
        );
 return "admin/adminscholarship-list";
    }

   
    // Edit Scholarship
 @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id,
            Model model){


        ScholarshipBean scholarship =sArepo.findByScholarshipId(id);
model.addAttribute("scholarship",scholarship
        );
return "admin/update-scholarship";
    }


   
    // Update Scholarship
   

    @PostMapping("/update")
    public String update(
            @ModelAttribute("scholarship") ScholarshipBean scholarship,
            @RequestParam(value="oldDeadline",
                    required=false) String oldDeadline){

 if(scholarship.getApplicationDeadline()==null
                && oldDeadline!=null
                && !oldDeadline.isEmpty()){

  scholarship.setApplicationDeadline(
                    LocalDate.parse(oldDeadline)
            );

        }
 scholarship.setUpdatedAt(
                LocalDateTime.now()
        );



        sArepo.update(scholarship);

  return "redirect:/admin/list";
    }



    // Show Applicants By Scholarship
 
    @GetMapping("/scholarship/{id}/applications")
    public String scholarshipApplications(
            @PathVariable String id,
            Model model){



        model.addAttribute("applications",sArepo.getApplicationsByScholarship(id)
        );
 return "admin/scholarshipapplication-list";
    }


    // Student Application Detail
 @GetMapping("/application/detail/{id}")
    public String applicationDetail(@PathVariable String id,Model model){

        ScholarshipApplicationBean application =sArepo.getApplicationDetail(id);
 model.addAttribute("appdetail",application);
 
  return "admin/scholarshipapplication-detail";
    }



    // Approve / Reject
   
 @PostMapping("/application/status")
    public String updateStatus( @RequestParam("applicationID")
            String applicationID, @RequestParam("status")
            String status, @RequestParam(value="reviewNotes", required=false)
String reviewNotes,@RequestParam("scholarshipID")
  String scholarshipID){
// Later replace with session admin ID
        String adminID =
                "19dda97d-7acd-11f1-898e-e4b97a5cf834";

 
////1. Update the application status (Approved / Rejected)
// sArepo.updateApplicationStatus(applicationID, status, adminID, reviewNotes);
// 
// // 2. AUTOMATION LOGIC: If the admin approved the student, check maximum recipients
// if ("APPROVED".equalsIgnoreCase(status)) { // Make sure this matches your status string (e.g., "Approved" or "APPROVED")
//     
//     // Fetch the specific scholarship to get its maxRecipients
//     ScholarshipBean scholarship = sArepo.findByScholarshipId(scholarshipID);
//     
//     // Get the current total number of approved students for this scholarship
//     int currentApprovedCount = sArepo.getApprovedCountByScholarship(scholarshipID);
//     
//     // If the max limit is reached or exceeded, turn the status to inactive
//     if (currentApprovedCount >= scholarship.getMaxRecipients()) {
//         sArepo.updateScholarshipStatus(scholarshipID, "INACTIVE"); // Or use 0 if your database flag is an Integer
//     }
// }
//        // return same scholarship applicants
//        return "redirect:/admin/scholarship/"
//                + scholarshipID
//                + "/applications";
//    }
     // Update selected application
        sArepo.updateApplicationStatus(applicationID, status, adminID, reviewNotes);

        // Only check when approving
        if ("APPROVED".equalsIgnoreCase(status)) {

            ScholarshipBean scholarship = sArepo.findByScholarshipId(scholarshipID);

            int approvedCount = sArepo.getApprovedCountByScholarship(scholarshipID);

            // Maximum reached
            if (approvedCount >= scholarship.getMaxRecipients()) {

                // 1. Scholarship inactive
                sArepo.updateScholarshipStatus(scholarshipID, "INACTIVE");

                // 2. Reject every pending application
                sArepo.rejectPendingApplications(
                        scholarshipID,
                        adminID,
                        "Automatically rejected because scholarship quota has been filled."
                );
            }
        }
            return "redirect:/admin/scholarship/" + scholarshipID + "/applications";
        
 }
}