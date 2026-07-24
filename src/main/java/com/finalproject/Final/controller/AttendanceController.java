package com.finalproject.Final.controller;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.finalproject.Final.model.AttendanceBean;
import com.finalproject.Final.model.AttendanceFormBean;
import com.finalproject.Final.model.ScheduleBean;
import com.finalproject.Final.model.UserBean;
import com.finalproject.Final.repository.AttendanceRepository;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @GetMapping("/{courseID}/{scheduleID}")
    public String attendancePage(
            @PathVariable String courseID,
            @PathVariable String scheduleID,
            Model model) {

        AttendanceFormBean form = new AttendanceFormBean();

        form.setAttendances(
                attendanceRepository.getStudentList(courseID, scheduleID)
        );

        model.addAttribute("attendanceForm", form);

        return "teacher/attendance";
    }
    @PostMapping("/save")
    public String saveAttendance(
            @ModelAttribute AttendanceFormBean attendanceForm,HttpSession session) {
    
    	
    	  UserBean loginUser =
    	            (UserBean) session.getAttribute("loginUser");

    	    String teacherID = loginUser.getUserID();


    	    for (AttendanceBean attendance : attendanceForm.getAttendances()) {

                attendance.setCheckInTime(LocalTime.now());

                if(attendanceRepository.attendanceExists(
                        attendance.getScheduleID(),
                        attendance.getUserID())){

                    attendanceRepository.updateAttendance(attendance,teacherID);

                }else{

                attendanceRepository.saveAttendance(attendance,teacherID);

            }
        }

        return "redirect:/attendance/list";
    }
    @GetMapping("/list")
    public String attendanceHome(Model model,HttpSession session) {
    	
    	 UserBean loginUser = (UserBean) session.getAttribute("loginUser");

 	    String teacherID = loginUser.getUserID();
     
        model.addAttribute(
                "courseList",
                attendanceRepository.getTeacherCourses(teacherID)
        );

        return "teacher/attendance-course-list";
    }
    @GetMapping("/schedule/{courseID}")
    public String scheduleList(@PathVariable String courseID, Model model){

        model.addAttribute(
                "scheduleList",
                attendanceRepository.getScheduleByCourse(courseID)
        );

        model.addAttribute("courseID", courseID);

        return "teacher/attendance-schedule-list";
    }
    @GetMapping("/mark/{courseID}/{scheduleID}")
    public String markAttendance(
            @PathVariable String courseID,
            @PathVariable String scheduleID,
            Model model) {

        AttendanceFormBean form = new AttendanceFormBean();

        form.setAttendances(
                attendanceRepository.getStudentList(courseID, scheduleID)
        );

        model.addAttribute("attendanceForm", form);

        return "teacher/attendance";
    }
    @GetMapping("/calendar/{courseID}")
    public String attendanceCalendar(
            @PathVariable String courseID,
            Model model) {

        List<ScheduleBean> scheduleList =
                attendanceRepository.getScheduleByCourse(courseID);

        model.addAttribute("courseID", courseID);
        model.addAttribute("scheduleList", scheduleList);

        return "teacher/attendance-calendar";
    }
    
    @GetMapping("/student/{userID}")
    public String studentAttendance(
            @PathVariable String userID,
            Model model){


        model.addAttribute(
            "attendanceList",
            attendanceRepository
            .getStudentAttendanceHistory(userID)
        );


        return "teacher/student-attendance-history";

    }
    @GetMapping("/students/{courseID}")
    public String studentList(
            @PathVariable String courseID,
            Model model){

    	 System.out.println("Course ID = " + courseID);
        model.addAttribute(
            "studentList",
            attendanceRepository
            .getStudentListByCourse(courseID)
        );


        model.addAttribute(
            "courseID",
            courseID
        );


        return "teacher/student-list";

    }
    @GetMapping("/students")
    public String studentCourseList(Model model,HttpSession session){

    	 UserBean loginUser = (UserBean) session.getAttribute("loginUser");

  	    String teacherID = loginUser.getUserID();


        model.addAttribute(
                "courseList",
                attendanceRepository.getTeacherCourses(teacherID)
        );


        return "teacher/student-course-list";
    }

}