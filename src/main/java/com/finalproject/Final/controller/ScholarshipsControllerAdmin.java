package com.finalproject.Final.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.finalproject.Final.model.ScholarshipApplicationBean;
import com.finalproject.Final.model.ScholarshipBean;
import com.finalproject.Final.model.UserBean;
import com.finalproject.Final.repository.ScholarshipApplicationRepository;
import com.finalproject.Final.repository.ScholarshipsRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class ScholarshipsControllerAdmin {

	
	@Autowired
	private ScholarshipsRepository schRepo;
	
	@Autowired
	private ScholarshipApplicationRepository sArepo;
	
	//insert for new scholarship
	@GetMapping("/scholarship-create")
    public String create(Model model) {

        model.addAttribute("scholarship",
                new ScholarshipBean());

        return "admin/create-scholarship";
    }
//insert for new  scholarship 
    @PostMapping("/save")
    public String save(@ModelAttribute ScholarshipBean scholarship,
                       HttpSession session) {

        UserBean loginUser =
                (UserBean) session.getAttribute("loginUser");

        scholarship.setCreatedByUserID(loginUser.getUserID());

        scholarship.setCreatedAt(LocalDateTime.now());
        scholarship.setUpdatedAt(LocalDateTime.now());

        sArepo.insert(scholarship);

        return "redirect:/admin/scholarship-create";
    }
    
    //view all scholarships 
    @GetMapping("/list")
    public String list(Model model){

        model.addAttribute(
                "scholarships",
                sArepo.getAll()
        );

        return "admin/adminscholarship-list";
    }
    
    
   // edit scholarship 
   @GetMapping("/edit/{id}")
   public String edit(
            @PathVariable("id") String id,
           Model model){
ScholarshipBean scholarship = sArepo.findByScholarshipId(id);
model.addAttribute("scholarship",scholarship);

 return "admin/update-scholarship";
        }
    
    
 // Update Scholarship
 @PostMapping("/update")
    public String update(
            @ModelAttribute ScholarshipBean scholarship){
	 

	 scholarship.setUpdatedAt(LocalDateTime.now());

 sArepo.update(scholarship);
 return "redirect:/admin/list";

    }

 
 
 // 1. Admin View All  Scholarship Applications
@GetMapping("/applications")
 public String viewApplications(Model model){
 model.addAttribute("applications",sArepo.getAllApplications()
     );
return "admin/scholarshipapplication-list";
   }
 

 // 2. Admin View Application Detail
@GetMapping("/application/detail/{id}")
 public String applicationDetail(
         @PathVariable("id") String id,
         Model model){

	
     ScholarshipApplicationBean application =
             sArepo.getApplicationDetail(id);
     
    	//System.out.println(application.getUserName());
    	//System.out.println(application.getScholarshipName());
model.addAttribute(
             "application",
             application
     );
 return "admin/scholarshipapplication-detail";


 }


// 3. Admin Approve / Reject
@PostMapping("/application/status")
public String updateStatus(@RequestParam("applicationID")
        String applicationID,
        @RequestParam("status")
        String status,
@RequestParam(value="reviewNotes",
 required=false)
String reviewNotes
){
//if admin is login //
//	UserBean admin =
//	 (UserBean) session.getAttribute("loginUser");
//	 String adminID = admin.getUserID();
//	       
	String adminID = "19dab99f-7acd-11f1-898e-e4b97a5cf834"; 
    // replace with session admin id

    sArepo.updateApplicationStatus(
applicationID,
status,
adminID,
 reviewNotes

    );



    return "redirect:/admin/applications";


}
}
	
	

