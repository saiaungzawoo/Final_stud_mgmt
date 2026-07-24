package com.finalproject.Final.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finalproject.Final.model.FinalGradeBean;
import com.finalproject.Final.model.UserBean;
import com.finalproject.Final.repository.FinalGradeRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/teacher/final-grade")
public class FinalGradeController {


    private final FinalGradeRepository finalGradeRepo;


    public FinalGradeController(
            FinalGradeRepository finalGradeRepo
    ) {
        this.finalGradeRepo = finalGradeRepo;
    }



    // Step 1 : Show course list
  

    @GetMapping("/student/{courseId}")
    public String studentList(
            @PathVariable String courseId,
            Model model
    ) {


        model.addAttribute(
            "studentList",
            finalGradeRepo.getStudentByCourse(courseId)
        );


        return "teacher/final-grade";
    }
    
  
  
    @PostMapping("/finalize")
    public String finalizeGrade(
            @ModelAttribute FinalGradeBean grade,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {


        UserBean loginUser =
                (UserBean) session.getAttribute("loginUser");


        String teacherId =
                loginUser.getUserID();



        grade.setFinalGradeID(
                UUID.randomUUID().toString()
        );


        grade.setFinalizedByID(
                teacherId
        );


        grade.setFinalizedAt(
                LocalDateTime.now()
        );


        finalGradeRepo.saveFinalGrade(grade);



        redirectAttributes.addFlashAttribute(
                "success",
                "Final Grade Saved Successfully"
        );


        return "redirect:/teacher/final-grade";
    }
    // Final Grade Main Page
    @GetMapping
    public String finalGradePage(
            @RequestParam(value="courseId", required=false) String courseId,
            HttpSession session,
            Model model
    ) {


        UserBean loginUser =
                (UserBean) session.getAttribute("loginUser");


        String teacherId =
                loginUser.getUserID();



        // Course List

        model.addAttribute(
                "courseList",
                finalGradeRepo.getTeacherCourses(teacherId)
        );



        // Course selected

        if(courseId != null && !courseId.isEmpty()) {


            model.addAttribute(
                    "selectedCourse",
                    courseId
            );


            model.addAttribute(
                    "studentList",
                    finalGradeRepo.getStudentByCourse(courseId)
            );

        }



        return "teacher/final-grade";
    }







    // View Student Final Result

    @GetMapping("/view/{enrollmentId}")
    public String viewFinalGrade(
            @PathVariable String enrollmentId,
            Model model,
            HttpSession session
    ) {


        UserBean loginUser =
                (UserBean) session.getAttribute("loginUser");



        String teacherId =
                loginUser.getUserID();



        model.addAttribute(
                "courseList",
                finalGradeRepo.getTeacherCourses(teacherId)
        );



        model.addAttribute(
                "grade",
                finalGradeRepo.calculateFinalGrade(enrollmentId)
        );


        return "teacher/final-grade";
    }

}

