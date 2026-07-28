package com.finalproject.Final.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finalproject.Final.model.ScheduleBean;
import com.finalproject.Final.model.UserBean;
import com.finalproject.Final.repository.AttendanceRepository;
import com.finalproject.Final.repository.ScheduleRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleRepository scheduleRepo;

    @Autowired
    private AttendanceRepository attenRepo;

    // 1. Teacher Course List
    @GetMapping("/list-course")
    public String scheduleCourseList(Model model, HttpSession session) {

        UserBean loginUser = (UserBean) session.getAttribute("loginUser");
        String teacherID = loginUser.getUserID();

        model.addAttribute(
                "courseList",
                attenRepo.getTeacherCourses(teacherID)
        );

        return "teacher/schedule-course-list";
    }

    // 2. Calendar View ပါဝင်သော Generate Page
    @GetMapping("/generate/{courseID}")
    public String generatePage(@PathVariable String courseID, Model model) {

        ScheduleBean bean;

        if (scheduleRepo.scheduleExists(courseID)) {
            bean = scheduleRepo.getFirstSchedule(courseID);
            bean.setStartDate(bean.getScheduleDate());
            bean.setRepeatDays(scheduleRepo.getRepeatDays(courseID));
            model.addAttribute("isUpdate", true);
            model.addAttribute("hasSchedule", true);

        } else {
            bean = new ScheduleBean();
            bean.setCourseId(courseID);
            model.addAttribute("isUpdate", false);
            model.addAttribute("hasSchedule", false);
        }

        model.addAttribute("schedule", bean);

        return "teacher/schedule-generate";
    }

    // 3. Generate Schedule
    @PostMapping("/generate")
    public String generateSchedule(@ModelAttribute("schedule") ScheduleBean obj) {

        generateScheduleData(obj);

        return "redirect:/schedule/generate/" + obj.getCourseId();
    }

    // 4. Update Schedule
 // 4. Update Schedule
    @PostMapping("/update")
    public String updateSchedule(
            @ModelAttribute("schedule") ScheduleBean bean,
            RedirectAttributes redirectAttributes) {

        // ၁။ Start Date ရောက်ပြီး/လွန်သွားပြီဆိုရင် Update လုပ်ခွင့် မပေးပါ
        if (bean.getStartDate() != null && !bean.getStartDate().isAfter(LocalDate.now())) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Cannot update schedule! The course start date (" + bean.getStartDate() + ") has already arrived or passed."
            );

            return "redirect:/schedule/generate/" + bean.getCourseId();
        }

        // ၂။ Attendance ယူထားပြီးပါကလည်း Schedule ပြင်ခွင့် မပေးပါ
        if (scheduleRepo.attendanceExistsByCourse(bean.getCourseId())) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Attendance has already been recorded for this course. Schedule cannot be updated."
            );

            return "redirect:/schedule/generate/" + bean.getCourseId();
        }

        // အထက်ပါ စည်းကမ်းချက်များနှင့် ငြိစွန်းခြင်း မရှိမှသာ ဖျက်ပြီး အသစ် ပြန် Generate လုပ်မည်
        scheduleRepo.deleteScheduleByCourse(bean.getCourseId());

        generateScheduleData(bean);

        redirectAttributes.addFlashAttribute(
                "success",
                "Schedule Updated Successfully."
        );

        return "redirect:/schedule/generate/" + bean.getCourseId();
    }
    // 5. AJAX API - Calendar ထဲ Event များ သန့်သန့်ပြသနိုင်ရန် JSON API
    @GetMapping("/events/{courseID}")
    @ResponseBody
    public List<Map<String, Object>> getCalendarEvents(@PathVariable String courseID) {
        List<ScheduleBean> schedules = scheduleRepo.findScheduleByCourse(courseID);
        List<Map<String, Object>> events = new ArrayList<>();

        for (ScheduleBean s : schedules) {
            Map<String, Object> event = new HashMap<>();
            String sId = s.getScheduleId();
            
            // Time Formatting
            String startTimeStr = (s.getStartTime() != null) ? s.getStartTime().toString() : "--:--";
            String endTimeStr = (s.getEndTime() != null) ? s.getEndTime().toString() : "--:--";

            event.put("id", sId);
            event.put("title", (s.getTopic() != null && !s.getTopic().trim().isEmpty()) ? s.getTopic() : "(No Topic)");
            
            // FullCalendar format YYYY-MM-DDT09:00:00
            event.put("start", s.getScheduleDate().toString() + "T" + startTimeStr);
            event.put("end", s.getScheduleDate().toString() + "T" + endTimeStr);
            
            Map<String, Object> props = new HashMap<>();
            props.put("room", s.getRoom() != null ? s.getRoom() : "");
            props.put("timeRange", startTimeStr + " - " + endTimeStr);
            props.put("rawTopic", s.getTopic() != null ? s.getTopic() : "");
            event.put("extendedProps", props);

            boolean hasTopic = s.getTopic() != null && !s.getTopic().trim().isEmpty();
            event.put("backgroundColor", hasTopic ? "#198754" : "#0d6efd");
            event.put("borderColor", hasTopic ? "#198754" : "#0d6efd");

            events.add(event);
        }

        return events;
    }
    // 6. AJAX API - Modal ထဲကနေ Topic Update လုပ်ရန်
    @PostMapping("/update-topic")
    @ResponseBody
    public ResponseEntity<String> updateTopic(
            @RequestParam("scheduleId") String scheduleId,
            @RequestParam("topic") String topic) {

        int updatedRows = scheduleRepo.updateTopic(scheduleId, topic);

        if (updatedRows > 0) {
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed");
        }
    }

    // Helper Method
    private void generateScheduleData(ScheduleBean obj) {

        String courseId = obj.getCourseId();
        int weeks = scheduleRepo.getDurationWeeks(courseId);
        LocalDate currentDate = obj.getStartDate();

        for (int week = 0; week < weeks; week++) {

            for (String day : obj.getRepeatDays()) {

                DayOfWeek targetDay = DayOfWeek.valueOf(day);

                while (currentDate.getDayOfWeek() != targetDay) {
                    currentDate = currentDate.plusDays(1);
                }

                ScheduleBean schedule = new ScheduleBean();

                schedule.setScheduleId(UUID.randomUUID().toString());
                schedule.setCourseId(courseId);
                schedule.setScheduleDate(currentDate);
                schedule.setStartTime(obj.getStartTime());
                schedule.setEndTime(obj.getEndTime());
                schedule.setRoom(obj.getRoom());
                schedule.setTopic(""); // Blank Topic
                schedule.setStatus("Scheduled");

                scheduleRepo.insertSchedule(schedule);

                currentDate = currentDate.plusDays(1);
            }

            currentDate = obj.getStartDate().plusWeeks(week + 1);
        }
    }
}