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
import com.finalproject.Final.model.EnrollmentBean;
import com.finalproject.Final.model.ScheduleBean;
import com.finalproject.Final.model.UserBean;
import com.finalproject.Final.service.CourseCategoryService;
import com.finalproject.Final.service.CourseService;
import com.finalproject.Final.service.EnrollmentService;
import com.finalproject.Final.service.ScheduleService;
import com.finalproject.Final.service.UserService;
import com.finalproject.Final.util.UserCodeUtil;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/student")
public class StudentController {
  
	@Autowired
	private CourseCategoryService courseCategoryService;
  
  @Autowired
  private EnrollmentService enrollmentService;
  
  @Autowired
  private CourseService courseService;
  
  @Autowired
  private ScheduleService scheduleService;
  
  
  @GetMapping("/portal")
  public String showStudentHome(Model model, HttpSession session) {

//      UserBean student = userService.findById(5); // temporary hardcoded
    
     UserBean student = (UserBean)session.getAttribute("loginUser");
     
     //test
     System.out.println("LOGIN USER:");
        System.out.println(student);
        
        System.out.println("USER ID:");
        System.out.println(student.getUserID());

      List<CourseBean> courses =  enrollmentService.getEnrolledCourses(student.getUserID());
//             
      //test
      System.out.println("COURSE COUNT: " + courses.size());

      model.addAttribute("student", student);
      model.addAttribute("studentCode", student.getUserCode());
//      model.addAttribute("studentCode", 
//          UserCodeUtil.formatUserCode(student.getRoleID(), student.getUserID()));
             
      model.addAttribute("courses", courses);
      model.addAttribute("enrolledCoursesCount", courses.size());

      return "student/student-home";
  }
  
  
  @GetMapping("/courses")
  public String myCourses(
		  @RequestParam(required=false) String keyword,
	        @RequestParam(required=false) String categoryId,
		  Model model, HttpSession session){

      UserBean student =
              (UserBean) session.getAttribute("loginUser");
      
     

//      List<CourseBean> courses =
//              enrollmentService.getEnrolledCourses(student.getUserID());
      
      List<CourseBean> courses;


      if((keyword == null || keyword.isBlank())
              &&
         (categoryId == null || categoryId.isBlank())) {


          // Normal page load
          courses =
              enrollmentService.getEnrolledCourses(
                      student.getUserID()
              );


      } else {


          // Search/filter
          courses =
              enrollmentService.searchMyCourses(
                      student.getUserID(),
                      keyword,
                      categoryId
              );

      }

      model.addAttribute("courses", courses);
      
      model.addAttribute(
              "categories",
              courseCategoryService.getAllCategories()
      );


      model.addAttribute(
              "keyword",
              keyword
      );


      model.addAttribute(
              "selectedCategory",
              categoryId
      );

      return "student/student-courses";
  }
  
  @GetMapping("/course/{id}")
  public String showStudentCourseDetail(
          @PathVariable String id,
          Model model,
          HttpSession session) {


      UserBean student =
              (UserBean) session.getAttribute("loginUser");


      CourseBean course =
              courseService.getById(id);


      List<ScheduleBean> schedules =
              scheduleService.getByCourseId(id);
      
    //sort schedules
      if (schedules != null) {
          schedules = schedules.stream()
              .sorted(Comparator.comparing(ScheduleBean::getScheduleDate)
                        .thenComparing(ScheduleBean::getStartTime))
              .collect(Collectors.toList());
      }


      model.addAttribute(
              "course",
              course
      );


      model.addAttribute(
              "schedules",
              schedules
      );


      model.addAttribute(
              "student",
              student
      );


      return "student/student-course-detail";
  }
  
  

}