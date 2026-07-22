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
import com.finalproject.Final.repository.SubmissionRepository;

@Controller
@RequestMapping("/teacher/submission")
public class TeacherSubmissionController {

    private final SubmissionRepository submissionRepo;
    private final String TEACHER_ID = "00ee5b4b-7a6f-11f1-8f4f-183d2d227d02"; // Temporary Teacher ID

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
            Model model) {

        //  Course List 
        List<CourseBean> courses = submissionRepo.getTeacherCourses(TEACHER_ID);
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
    public String gradeSubmission(@ModelAttribute SubmissionBean bean, @RequestParam(required = false) String courseID, @RequestParam(required = false) String assignmentID) {

        bean.setGradedByID(TEACHER_ID);
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
    public String selectCourse(Model model) {
        String teacherID = "00ee5b4b-7a6f-11f1-8f4f-183d2d227d02";
        List<CourseBean> courses = submissionRepo.getTeacherCourses(teacherID);
        model.addAttribute("courses", courses);

        return "teacher/submission-dashboard"; 
    }
}