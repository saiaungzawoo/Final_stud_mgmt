package com.finalproject.Final.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.finalproject.Final.model.AssignmentBean;
import com.finalproject.Final.model.AssignmentStatus;
import com.finalproject.Final.repository.AssignmentRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/assignment")
public class AssignmentController {

    @Autowired
    private AssignmentRepository assignmentRepo;

    // Create Page
    @GetMapping("/create")
    public String createPage(Model model) {

        AssignmentBean bean = new AssignmentBean();

        // Default Status
        bean.setStatus(AssignmentStatus.Draft);

        // Temporary Teacher ID
        String teacherID = "00ee5b4b-7a6f-11f1-8f4f-183d2d227d02";

        model.addAttribute("assignment", bean);

        model.addAttribute(
                "courseList",
                assignmentRepo.getTeacherCourses(teacherID)
        );

        model.addAttribute(
                "statusList",
                AssignmentStatus.values()
        );

        return "teacher/assignment-create";
    }

    // Save
    @PostMapping("/save")
    public String saveAssignment(
            @Valid @ModelAttribute("assignment") AssignmentBean bean,
            BindingResult result,
            Model model) {


        System.out.println("========== SAVE METHOD START ==========");


        System.out.println("ID : " + bean.getAssignmentID());
        System.out.println("TITLE : " + bean.getTitle());
        System.out.println("COURSE : " + bean.getCourseID());


        if(result.hasErrors()) {


            System.out.println("VALIDATION ERROR");


            result.getFieldErrors()
                  .forEach(error -> {

                      System.out.println(
                          error.getField()
                          + " : "
                          + error.getDefaultMessage()
                      );

                  });


            String teacherID =
                "00ee5b4b-7a6f-11f1-8f4f-183d2d227d02";


            model.addAttribute(
                "courseList",
                assignmentRepo.getTeacherCourses(teacherID)
            );


            model.addAttribute(
                "statusList",
                AssignmentStatus.values()
            );


            return "teacher/assignment-create";
        }



        // INSERT
        if(bean.getAssignmentID() == null ||
           bean.getAssignmentID().isEmpty()) {


            System.out.println("INSERT MODE");


            bean.setAssignmentID(
                UUID.randomUUID().toString()
            );


            bean.setCreatedByID(
                "00ee5b4b-7a6f-11f1-8f4f-183d2d227d02"
            );


            assignmentRepo.saveAssignment(bean);


        }
        // UPDATE
        else {


            System.out.println("UPDATE MODE");


            assignmentRepo.updateAssignment(bean);

        }



        System.out.println("========== SAVE SUCCESS ==========");


        return "redirect:/assignment/list";

    }
    // Assignment List
    @GetMapping("/list")
    public String assignmentList(Model model) {

        String teacherID =
                "00ee5b4b-7a6f-11f1-8f4f-183d2d227d02";

        model.addAttribute(
                "assignmentList",
                assignmentRepo.getAssignmentList(teacherID)
        );

        return "teacher/assignment-list";
    }
    @GetMapping("/edit/{id}")
    public String editPage(
            @PathVariable String id,
            Model model) {


        AssignmentBean bean =
                assignmentRepo.getAssignmentById(id);


        String teacherID =
                "00ee5b4b-7a6f-11f1-8f4f-183d2d227d02";


        model.addAttribute(
                "assignment",
                bean
        );


        model.addAttribute(
                "courseList",
                assignmentRepo.getTeacherCourses(teacherID)
        );


        model.addAttribute(
                "statusList",
                AssignmentStatus.values()
        );


        model.addAttribute(
                "isUpdate",
                true
        );


        return "teacher/assignment-create";

    }

}