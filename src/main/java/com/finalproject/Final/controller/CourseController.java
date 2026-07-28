package com.finalproject.Final.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.model.ScheduleBean;
import com.finalproject.Final.service.CourseService;
import com.finalproject.Final.service.ScheduleService;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ScheduleService scheduleService;

    // Show all courses
    @GetMapping("/show")
    public String showCourses(
    		 @RequestParam(required = false) String keyword,
    		Model model) {
    	
    	List<CourseBean> courses;


        if(keyword != null && !keyword.isBlank()) {

            courses =
                courseService.searchStudentCourses(keyword);

        }
        else {

            courses =
                courseService.getAllCourses();

        }


        model.addAttribute(
                "courses",
                courses
        );
    	
    	
//        model.addAttribute("courses", courseService.getAllCourses());
        return "student/courses";
    }

    // Show course detail page
    @GetMapping("/{id}")
    public String showCourseDetail(@PathVariable String id, Model model) {

        CourseBean course = courseService.getById(id);
        List<ScheduleBean> schedules = scheduleService.getByCourseId(id);
        
        //sort schedules
        if (schedules != null) {
            schedules = schedules.stream()
                .sorted(Comparator.comparing(ScheduleBean::getScheduleDate)
                          .thenComparing(ScheduleBean::getStartTime))
                .collect(Collectors.toList());
        }
        
        
        if (course.getSeatsAvailable() == 0) {
            course.setStatus("FULL");
        }

        model.addAttribute("course", course);
        model.addAttribute("schedules", schedules);

        return "student/course-detail";
    }
    
    //filter courses by category
    @GetMapping("/by-category/{id}")
    public String showCoursesByCategory(
            @PathVariable String id,
            Model model){

        model.addAttribute(
                "courses",
                courseService.getByCategory(id)
        );

        return "student/courses";
    }
}