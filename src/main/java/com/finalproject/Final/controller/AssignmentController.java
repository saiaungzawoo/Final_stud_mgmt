package com.finalproject.Final.controller;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finalproject.Final.model.AssignmentBean;
import com.finalproject.Final.model.AssignmentStatus;
import com.finalproject.Final.model.UserBean;
import com.finalproject.Final.repository.AssignmentRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/assignment")
public class AssignmentController {

    @Autowired
    private AssignmentRepository assignmentRepo;

  

    @GetMapping("/list")
    public String assignmentDashboard(
            @RequestParam(value = "courseID", required = false) String courseID,
            Model model,HttpSession session) {
    	
    	 UserBean loginUser = (UserBean) session.getAttribute("loginUser");
    	 String teacherID = loginUser.getUserID();
    	 
    	 assignmentRepo.updateClosedAssignments();
 	    
    	 if (!model.containsAttribute("assignment")) {
    		    AssignmentBean bean = new AssignmentBean();
    		    model.addAttribute("assignment", bean);
    		}

        if (courseID != null && !courseID.isEmpty()) {
            model.addAttribute("assignmentList", assignmentRepo.getAssignmentListByCourse(courseID));
        } else {
            model.addAttribute("errorMessage", "Please select a course to view assignments.");
        }


        model.addAttribute("courseList", assignmentRepo.getTeacherCourses(teacherID));
        model.addAttribute("statusList", 
                new AssignmentStatus[]{
                    AssignmentStatus.Draft,
                    AssignmentStatus.Published
                });
        return "teacher/assignment-dashboard"; //  Single-page HTML Name
    }

   
    @PostMapping("/save")
    public String saveAssignment(
            @Valid @ModelAttribute("assignment") AssignmentBean bean,
            BindingResult result,HttpSession session,
            RedirectAttributes redirectAttributes) {

        // validation input error 
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.assignment", result);
            redirectAttributes.addFlashAttribute("assignment", bean);
            return "redirect:/assignment/list?courseID=" + bean.getCourseID();
        }

        if (bean.getAssignmentID() == null || bean.getAssignmentID().isEmpty()) {
            bean.setAssignmentID(UUID.randomUUID().toString());
           
        }
        
        UserBean loginUser = (UserBean) session.getAttribute("loginUser");
   	 String teacherID = loginUser.getUserID();
	    
        bean.setCreatedByID(teacherID ); // Teacher ID 
        assignmentRepo.saveAssignment(bean); // DB 

        return "redirect:/assignment/list?courseID=" + bean.getCourseID();
    }

   
    @PostMapping("/update")
    public String updateAssignment(@ModelAttribute("assignment") AssignmentBean bean,HttpSession session) {
    	  UserBean loginUser = (UserBean) session.getAttribute("loginUser");
    	   	 String teacherID = loginUser.getUserID();
        bean.setCreatedByID(teacherID);
        
        assignmentRepo.updateAssignment(bean); // DB ထဲမှာ သွားပြင်မယ်

        //Course list 
        return "redirect:/assignment/list?courseID=" + bean.getCourseID();
    }



    @GetMapping("/course-select")
    public String redirectOldSelect() {
        return "redirect:/assignment/list";
    }

    @GetMapping("/create")
    public String redirectOldCreate() {
        return "redirect:/assignment/list";
    }
    
}