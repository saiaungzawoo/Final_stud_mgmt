package com.finalproject.Final.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.finalproject.Final.model.AssignmentBean;
import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.model.SubmissionBean;
import com.finalproject.Final.model.UserBean;
import com.finalproject.Final.repository.SubmissionRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/teacher/submission")
public class TeacherSubmissionController {

    private final SubmissionRepository submissionRepo;
   

    public TeacherSubmissionController(SubmissionRepository submissionRepo) {
        this.submissionRepo = submissionRepo;
    }

    /**
     * 1. Core Single Page Dashboard Router
     *
     */
    @GetMapping("/dashboard")
    public String showDashboard(
            @RequestParam(required = false) String courseID,
            @RequestParam(required = false) String assignmentID,
            @RequestParam(required = false) String submissionID,
            Model model,HttpSession session) {

    	
    	 UserBean loginUser = (UserBean) session.getAttribute("loginUser");

  	    String teacherID = loginUser.getUserID();
  	    
        //  Course List 
        List<CourseBean> courses = submissionRepo.getTeacherCourses(teacherID);
        model.addAttribute("courses", courses);

        // Course  Assignment List
        if (courseID != null && !courseID.isEmpty()) {
            List<AssignmentBean> assignments = submissionRepo.getAssignmentByCourse(courseID);
            model.addAttribute("assignments", assignments);
            model.addAttribute("selectedCourseId", courseID);
        }

        // Assignment Student Submissions
        if (assignmentID != null && !assignmentID.isEmpty()) {
            List<SubmissionBean> submissions = submissionRepo.getSubmissionByAssignment(assignmentID);
            model.addAttribute("submissions", submissions);
            model.addAttribute("selectedAssignmentId", assignmentID);
        }

        // Grade  Detail 
        if (submissionID != null && !submissionID.isEmpty()) {
            SubmissionBean submission = submissionRepo.getSubmissionDetail(submissionID);
            model.addAttribute("submission", submission);
        }

        return "teacher/submission-dashboard"; //HTML File 
    }

    /**
     * 2. Grade Update Action


     */
    @PostMapping("/grade")
    public String gradeSubmission(@ModelAttribute SubmissionBean bean, @RequestParam(required = false) String courseID, @RequestParam(required = false) String assignmentID,HttpSession session) {
    	
    	 UserBean loginUser = (UserBean) session.getAttribute("loginUser");

  	    String teacherID = loginUser.getUserID();
        bean.setGradedByID(teacherID);
        submissionRepo.updateGrade(bean);



        return "redirect:/teacher/submission/dashboard?courseID=" + courseID + "&assignmentID=" + assignmentID;
    }
    @GetMapping("/assignment/{courseID}")
    public String selectAssignment(
            @PathVariable("courseID") String courseID, // ("courseID") 

            Model model) {

        List<AssignmentBean> assignments = submissionRepo.getAssignmentByCourse(courseID);
        model.addAttribute("assignments", assignments);

        return "teacher/submission-dashboard"; // HTML နာမည်
    }
    @GetMapping("/list/{assignmentID}")
    public String submissionList(
            @PathVariable("assignmentID") String assignmentID, 
            Model model) {
    
    	if (assignmentID != null && !assignmentID.isEmpty()) {
    	    List<SubmissionBean> submissions = submissionRepo.getSubmissionByAssignment(assignmentID);
    	    model.addAttribute("submissions", submissions);
    	    
    	    // 💡 အသစ်ထည့်ရန် - အမှတ်ပေးပြီးသား (score မဟုတ်တာ) အရေအတွက်ကို Java ဘက်ကနေ ကြိုတွက်ပေးလိုက်ခြင်း
    	    long gradedCount = submissions.stream()
    	                                  .filter(sub -> sub.getScore() != null)
    	                                  .count();
    	    model.addAttribute("gradedCount", gradedCount);
    	}
        List<SubmissionBean> submissions = submissionRepo.getSubmissionByAssignment(assignmentID);
        model.addAttribute("submissions", submissions);

        return "teacher/submission-dashboard"; 
    }
    @GetMapping("/detail/{submissionID}")
    public String submissionDetail(
            @PathVariable("submissionID") String submissionID, // ("submissionID") 
            Model model) {

        SubmissionBean submission = submissionRepo.getSubmissionDetail(submissionID);
        model.addAttribute("submission", submission);

       
        return "teacher/submission-dashboard"; 
    }
    @GetMapping("/course")
    public String selectCourse(Model model,HttpSession session) {
    	 UserBean loginUser = (UserBean) session.getAttribute("loginUser");

  	    String teacherID = loginUser.getUserID();
        List<CourseBean> courses = submissionRepo.getTeacherCourses(teacherID);
        model.addAttribute("courses", courses);

        return "teacher/submission-dashboard"; 
    }
}