package com.finalproject.Final.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finalproject.Final.model.ScheduleBean;
import com.finalproject.Final.repository.AttendanceRepository;
import com.finalproject.Final.repository.ScheduleRepository;

import org.springframework.ui.Model;


@Controller
@RequestMapping("/schedule")
public class ScheduleController {


    @Autowired
    private ScheduleRepository scheduleRepo;
    @Autowired
private AttendanceRepository attenRepo;


    // Generate Page
    @GetMapping("/generate")
    public String generatePage(Model model) {

        String courseID = "00f03923-7a6f-11f1-8f4f-183d2d227d02";

        ScheduleBean bean;

        if(scheduleRepo.scheduleExists(courseID)){

            bean = scheduleRepo.getFirstSchedule(courseID);

            bean.setStartDate(bean.getScheduleDate());

            bean.setTopicPrefix(
                    scheduleRepo.getTopicPrefix(courseID)
            );

            bean.setRepeatDays(
                    scheduleRepo.getRepeatDays(courseID)
            );

            model.addAttribute("isUpdate", true);

        }else{

            bean = new ScheduleBean();

            bean.setCourseId(courseID);

            model.addAttribute("isUpdate", false);
        }

        model.addAttribute("schedule", bean);

        return "teacher/schedule-generate";
    }

    // Generate Schedule
    @PostMapping("/generate")
    public String generateSchedule(
            @ModelAttribute("schedule") ScheduleBean obj){

        generateScheduleData(obj);

        return "redirect:/schedule/list";
    }
    private void generateScheduleData(ScheduleBean obj){

        String courseId = obj.getCourseId();

        int weeks = scheduleRepo.getDurationWeeks(courseId);

        LocalDate currentDate = obj.getStartDate();

        int lessonNo = 1;

        for(int week = 0; week < weeks; week++){

            for(String day : obj.getRepeatDays()){

                DayOfWeek targetDay = DayOfWeek.valueOf(day);

                while(currentDate.getDayOfWeek() != targetDay){
                    currentDate = currentDate.plusDays(1);
                }

                ScheduleBean schedule = new ScheduleBean();

                schedule.setScheduleId(UUID.randomUUID().toString());
                schedule.setCourseId(courseId);
                schedule.setScheduleDate(currentDate);
                schedule.setStartTime(obj.getStartTime());
                schedule.setEndTime(obj.getEndTime());
                schedule.setRoom(obj.getRoom());
                schedule.setTopic(obj.getTopicPrefix() + " " + lessonNo);
                schedule.setStatus("Scheduled");
                System.out.println("Insert Date = " + schedule.getScheduleDate());
                System.out.println("Insert Topic = " + schedule.getTopic());
                System.out.println("Insert Room = " + schedule.getRoom());
                scheduleRepo.insertSchedule(schedule);

                lessonNo++;
                currentDate = currentDate.plusDays(1);
            }

            currentDate = obj.getStartDate().plusWeeks(week + 1);
        }
    }
    @GetMapping("/list")
    public String scheduleList(Model model){

        List<ScheduleBean> schedules =
                scheduleRepo.findAllSchedule();


        model.addAttribute("schedules", schedules);


        return "teacher/schedule-list";
    }
    @PostMapping("/update")
    public String updateSchedule(
            @ModelAttribute("schedule") ScheduleBean bean,
            RedirectAttributes redirectAttributes) {
    
        if(scheduleRepo.attendanceExistsByCourse(bean.getCourseId())){

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Attendance has already been recorded for this course. Schedule cannot be updated."
            );

            return "redirect:/schedule/generate/" + bean.getCourseId();
        }

        scheduleRepo.deleteScheduleByCourse(bean.getCourseId());

        generateScheduleData(bean);

        redirectAttributes.addFlashAttribute(
                "success",
                "Schedule Updated Successfully."
        );

        return "redirect:/schedule/list";
    }
    @GetMapping("/list-course")
    public String scheduleCourseList(Model model){

        String teacherID = "00ee5b4b-7a6f-11f1-8f4f-183d2d227d02";

        model.addAttribute(
                "courseList",
                attenRepo.getTeacherCourses(teacherID)
        );

        return "teacher/schedule-course-list";
    }
    @GetMapping("/generate/{courseID}")
    public String generatePage(
            @PathVariable String courseID,
            Model model) {

    	  System.out.println("Course ID = " + courseID);
        ScheduleBean bean;


        if(scheduleRepo.scheduleExists(courseID)){

            bean = scheduleRepo.getFirstSchedule(courseID);
            
            //bean = scheduleRepo.getFirstSchedule(courseID);

            bean.setStartDate(bean.getScheduleDate());

           

            bean.setStartDate(bean.getScheduleDate());
            bean.setTopicPrefix(
                    scheduleRepo.getTopicPrefix(courseID)
            );

            bean.setRepeatDays(
                    scheduleRepo.getRepeatDays(courseID)
            );


            model.addAttribute("isUpdate", true);


        }else{


            bean = new ScheduleBean();

            bean.setCourseId(courseID);


            model.addAttribute("isUpdate", false);

        }


        model.addAttribute("schedule", bean);


        return "teacher/schedule-generate";
    }

}