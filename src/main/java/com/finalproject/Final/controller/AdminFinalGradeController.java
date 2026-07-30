package com.finalproject.Final.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.finalproject.Final.repository.FinalGradeRepository;


@Controller
@RequestMapping("/admin/final-grade")
public class AdminFinalGradeController {


    private final FinalGradeRepository finalGradeRepo;


    public AdminFinalGradeController(
            FinalGradeRepository finalGradeRepo) {

        this.finalGradeRepo = finalGradeRepo;
    }



    // Admin Final Grade View
    @GetMapping
    public String finalGradePage(
            @RequestParam(required = false) String courseId,
            Model model) {


        // Show all courses
        model.addAttribute(
                "courseList",
                finalGradeRepo.getAllCourses()
        );


        // When admin selects course
        if(courseId != null && !courseId.isEmpty()) {


            model.addAttribute(
                    "selectedCourse",
                    courseId
            );


            model.addAttribute(
                    "gradeList",
                    finalGradeRepo.getFinalGradesByCourse(courseId)
            );

        }


        return "admin/final-grade-list";
    }

}