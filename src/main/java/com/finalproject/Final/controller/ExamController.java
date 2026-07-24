package com.finalproject.Final.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.finalproject.Final.model.ExamBean;
import com.finalproject.Final.model.UserBean;
import com.finalproject.Final.repository.ExamRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/exam")
public class ExamController {
	
	@Autowired
	private ExamRepository examRepo;
	
	// Hardcoded teacher tracking profile token string used across actions


	@GetMapping("/list")
	public String examList(
	        @RequestParam(value="courseID", required=false) String courseID,
	        Model model,HttpSession session) {

		
		 UserBean loginUser = (UserBean) session.getAttribute("loginUser");

 	    String teacherID = loginUser.getUserID();
 	    

	    // Standard UI reference list populations needed by the creation/edition modals on this same view
	    model.addAttribute("courseList", examRepo.getTeacherCourses(teacherID));
	    model.addAttribute("examTypeList", new String[]{"Quiz", "Midterm", "Final", "Practical"});
	    model.addAttribute("statusList", new String[]{"Scheduled", "In Progress", "Completed", "Cancelled"});

	    // Form Backing Object instantiation to prevent initialization errors in the creation modal
	    ExamBean blankBean = new ExamBean();
	    blankBean.setStatus("Scheduled");
	    model.addAttribute("exam", blankBean);

	    if(courseID == null || courseID.isEmpty()) {
	        // If no course context has been passed, render the dashboard shell showing the empty state layout
	        model.addAttribute("examList", null);
	        return "teacher/exam-list";
	    }

	    // Populate records using repository parameters
	    model.addAttribute("examList", examRepo.getExamListByCourse(courseID));
	    return "teacher/exam-list";
	}

	@PostMapping("/save")
	public String saveExam(
	        @Valid @ModelAttribute("exam") ExamBean bean,
	        BindingResult result,
	        Model model,HttpSession session) {
		
		 UserBean loginUser = (UserBean) session.getAttribute("loginUser");

 	    String teacherID = loginUser.getUserID();

	    if(result.hasErrors()) {
	        // If inputs fail validation checks, reload the dashboard view with operational context lists intact
	        model.addAttribute("courseList", examRepo.getTeacherCourses(teacherID));
	        model.addAttribute("examTypeList", new String[]{"Quiz", "Midterm", "Final", "Practical"});
	      
	        // Keep invalid inputs visible to the user inside the modal layout configuration
	        model.addAttribute("openCreateModal", true); 
	        return "teacher/exam-list";
	    }

	    bean.setCreatedByID(teacherID);
	    bean.setStatus("Scheduled");
	    examRepo.saveExam(bean);

	    return "redirect:/exam/list?courseID=" + bean.getCourseID();
	}

	@PostMapping("/update")
	public String updateExam(@ModelAttribute("exam") ExamBean bean) {
	    examRepo.updateExam(bean);
	    return "redirect:/exam/list?courseID=" + bean.getCourseID();
	}

	// Delete configuration path directly receiving requests from the dashboard UI prompt module
	@GetMapping("/delete/{id}")
	public String deleteExam(@PathVariable("id") String examID, @RequestParam("courseID") String courseID) {
	    // Note: If you don't have a direct delete routine in the repo, update this to your native framework delete call
	    // examRepo.deleteExamByID(examID); 
	    return "redirect:/exam/list?courseID=" + courseID;
	}
	@GetMapping("/cancel/{id}")
	public String cancelExam(
	        @PathVariable("id") String examID,
	        @RequestParam("courseID") String courseID) {

	    examRepo.updateStatus(examID, "Cancelled");

	    return "redirect:/exam/list?courseID=" + courseID;
	}
}