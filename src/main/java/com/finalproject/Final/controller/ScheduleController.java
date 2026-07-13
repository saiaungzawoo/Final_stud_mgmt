package com.finalproject.Final.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.finalproject.Final.model.ScheduleBean;
import com.finalproject.Final.repository.ScheduleRepository;

import org.springframework.ui.Model;


@Controller
@RequestMapping("/schedule")
public class ScheduleController {


    @Autowired
    private ScheduleRepository scheduleRepo;



    // Generate Page
    @GetMapping("/generate")
    public String generatePage(Model model){

        ScheduleBean schedule = new ScheduleBean();

        // မင်းပေးထားတဲ့ courseID
        schedule.setCourseId(
            "00f03923-7a6f-11f1-8f4f-183d2d227d02"
        );

        model.addAttribute("schedule", schedule);

        return "teacher/schedule-generate";
    }



    // Generate Schedule
    @PostMapping("/generate")
    public String generateSchedule(
            @ModelAttribute("schedule") ScheduleBean obj
    ){


        String courseId = obj.getCourseId();


        // course duration ယူ
        int weeks = scheduleRepo.getDurationWeeks(courseId);



        LocalDate currentDate = obj.getStartDate();



        int lessonNo = 1;



        for(int week = 0; week < weeks; week++){


            for(String day : obj.getRepeatDays()){


                DayOfWeek targetDay =
                        DayOfWeek.valueOf(day);



                while(currentDate.getDayOfWeek() != targetDay){

                    currentDate = currentDate.plusDays(1);

                }



                ScheduleBean schedule =
                        new ScheduleBean();



                schedule.setScheduleId(
                    UUID.randomUUID().toString()
                );


                schedule.setCourseId(courseId);


                schedule.setScheduleDate(currentDate);


                schedule.setStartTime(
                    obj.getStartTime()
                );


                schedule.setEndTime(
                    obj.getEndTime()
                );


                schedule.setRoom(
                    obj.getRoom()
                );


                schedule.setTopic(
                    obj.getTopicPrefix()
                    +" "+lessonNo
                );


                schedule.setStatus("Scheduled");



                scheduleRepo.insertSchedule(schedule);



                lessonNo++;


                currentDate =
                    currentDate.plusDays(1);

            }


            // next week start
            currentDate =
                obj.getStartDate()
                .plusWeeks(week + 1);

        }


        return "redirect:/schedule/list";

    }
    @GetMapping("/list")
    public String scheduleList(Model model){

        List<ScheduleBean> schedules =
                scheduleRepo.findAllSchedule();


        model.addAttribute("schedules", schedules);


        return "teacher/schedule-list";
    }

}