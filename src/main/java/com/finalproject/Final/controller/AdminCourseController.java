package com.finalproject.Final.controller;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.service.CourseCategoryService;
import com.finalproject.Final.service.CourseService;
import com.finalproject.Final.service.SubCategoryService;
import com.finalproject.Final.service.TeacherService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin/courses")
public class AdminCourseController {


    @Autowired
    private CourseCategoryService courseCategoryService;
    
    @Autowired
    private SubCategoryService subCategoryService;
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private CourseService courseService;



    @GetMapping("/create")
    public String createCoursePage(Model model) {


        model.addAttribute(
                "course",
                new CourseBean()
        );


        model.addAttribute(
                "categories",
                courseCategoryService.getAllCategories()
        );
        
        model.addAttribute(
                "subcategories",
               subCategoryService.getAll()
        );
        
        model.addAttribute(
                "teachers",
                teacherService.getAllTeachers()
        );



        return "admin/admin-course-create";
    }

    
    @PostMapping("/create")
    public String createCourse(
            @ModelAttribute("course") CourseBean course, HttpSession session) {

        System.out.println("========== COURSE CREATE ==========");

        System.out.println("Course Name       : " + course.getName());
        System.out.println("Description       : " + course.getDescription());

        System.out.println("Category ID       : " + course.getCourseCategoryId());
        System.out.println("Subcategory ID    : " + course.getSubcategoryId());
        System.out.println("Teacher ID        : " + course.getTeacherId());

        System.out.println("Duration          : " + course.getDurationWeeks());
        System.out.println("Fee               : " + course.getFee());

        System.out.println("Level             : " + course.getLevel());
        System.out.println("Status            : " + course.getStatus());

        System.out.println("Seats Total       : " + course.getSeatsTotal());

        System.out.println("Allow Installment : " + course.getAllowedInstallment());
        System.out.println("Allow Scholarship : " + course.getAllowedScholarship());
        
        
     // Generate course UUID
        course.setCourseId(
                UUID.randomUUID().toString()
        );



        // Get logged-in admin ID
        String adminId =
                (String) session.getAttribute("userID");


        course.setCreatedBy(adminId);



        // Handle checkbox null values
        if(course.getAllowedInstallment() == null) {

            course.setAllowedInstallment(0);

        }


        if(course.getAllowedScholarship() == null) {

            course.setAllowedScholarship(0);

        }



        // Available seats = total seats initially
        course.setSeatsAvailable(
                course.getSeatsTotal()
        );



        System.out.println("Generated Course ID : " + course.getCourseId());
        System.out.println("Created By          : " + course.getCreatedBy());
        System.out.println("Available Seats     : " + course.getSeatsAvailable());

        System.out.println("===================================");
        
        courseService.createCourse(course);

        return "redirect:/admin/courses";
    }
}