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
            @RequestParam String courseId,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
      System.out.println("SAVE REMARKS = " + grade.getRemarks());

        if(finalGradeRepo.existsByEnrollmentID(
                grade.getEnrollmentID())) {


            redirectAttributes.addFlashAttribute(
                    "error",
                    "This student final grade is already finalized!"
            );


            return "redirect:/teacher/final-grade/view/"
            + grade.getEnrollmentID()
            + "?courseId="
            + courseId;
        }



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


        if(grade.getFinalScore() >= 60) {

            grade.setStatus("Completed");

        } else {

            grade.setStatus("Failed");

        }



        finalGradeRepo.saveFinalGrade(grade);



        redirectAttributes.addFlashAttribute(
                "success",
                "Final Grade Saved Successfully"
        );


        return "redirect:/teacher/final-grade/view/"
                + grade.getEnrollmentID();
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

            @RequestParam(value="courseId", required=false) String courseId,
            Model model,
            HttpSession session
    ) {
      FinalGradeBean test =
              finalGradeRepo.calculateFinalGrade(enrollmentId);
 System.out.println("STATUS = " + test.getStatus());
      System.out.println("REMARKS = " + test.getRemarks());
    

      model.addAttribute(
              "grade",
              test
      );
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
        model.addAttribute(
              "isFinalized",
              finalGradeRepo.existsByEnrollmentID(enrollmentId)
          );


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
    @GetMapping("/list")
    public String finalGradeList(Model model){


        model.addAttribute(
                "finalGradeList",
                finalGradeRepo.getAllFinalGrades()
        );


        return "admin/final-grade-list";

    }
    @GetMapping("/teacher-list")
    public String teacherFinalGradeList(
            Model model,
            HttpSession session
    ) {


        UserBean loginUser =
                (UserBean) session.getAttribute("loginUser");


        String teacherID =
                loginUser.getUserID();



        model.addAttribute(
                "finalGradeList",
                finalGradeRepo.getAllFinalGrades()
        );



        return "teacher/final-grade-list";

    }

}