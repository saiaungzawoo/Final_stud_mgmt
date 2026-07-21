package com.finalproject.Final.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.finalproject.Final.model.AnnouncementBean;
import com.finalproject.Final.repository.AnnouncementRepository;

@Controller
@RequestMapping("/announcement")
public class AnnouncementController {

    private final AnnouncementRepository announcementRepo;
    
    private final String TEACHER_ID = "00ee5b4b-7a6f-11f1-8f4f-183d2d227d02";

    public AnnouncementController(AnnouncementRepository announcementRepo) {
        this.announcementRepo = announcementRepo;
    }

    /**
     * ၁။ အဓိက Dashboard Page (Course list ပြခြင်းနှင့် ရွေးချယ်ထားသော Course ၏ Announcement list တွဲပြခြင်း)
     * URL: /announcement/list
     */
    @GetMapping("/list")
    public String announcementDashboard(
            @RequestParam(value = "courseID", required = false) String courseID,
            Model model) {

        //  Course List Grid Card
        model.addAttribute("courseList", announcementRepo.getTeacherCourses(TEACHER_ID));

        // Form Object Binding 
        AnnouncementBean bean = new AnnouncementBean();
        if (courseID != null && !courseID.isEmpty()) {
            bean.setCourseID(courseID); // Course  Modal  CourseID  Default 
            
            //  Course  Announcement List 
            List<AnnouncementBean> announcementList = announcementRepo.getAnnouncementsByCourse(courseID);
            model.addAttribute("announcementList", announcementList);
            model.addAttribute("courseID", courseID);
        }

        model.addAttribute("announcement", bean);

        return "teacher/announcement-dashboard"; 
    }

    /**
     * ၂။ Announcement အသစ်ဆောက်ခြင်းနှင့် တည်းဖြတ်ခြင်း (Save / Update Endpoint)
     * URL: /announcement/save
     */
    @PostMapping("/save")
    public String saveAnnouncement(@ModelAttribute("announcement") AnnouncementBean bean) {

        bean.setCreatedByID(TEACHER_ID);

      
        if (bean.getAnnouncementID() == null || bean.getAnnouncementID().isEmpty()) {
            bean.setAnnouncementID(java.util.UUID.randomUUID().toString());
            bean.setPublishDate(LocalDateTime.now());
            announcementRepo.saveAnnouncement(bean);
        } else {
            announcementRepo.updateAnnouncement(bean);
        }

        return "redirect:/announcement/list?courseID=" + bean.getCourseID();
    }

    /**
     * ၃။ လင့်ခ်ဟောင်းများ (Backward Compatibility Redirection)
     * အရင်က သုံးခဲ့ဖူးတဲ့ URL အဟောင်းတွေကို ဆရာက နှိပ်မိရင် Dashboard အသစ်ဆီ အလိုအလျောက် ပို့ပေးတာဖြစ်ပါတယ်
     */
    @GetMapping("/course-list")
    public String redirectOldCourseList() {
        return "redirect:/announcement/list";
    }

    @GetMapping("/list/{courseID}")
    public String redirectOldList(@PathVariable("courseID") String courseID) {
        return "redirect:/announcement/list?courseID=" + courseID;
    }

    @GetMapping("/create/{courseID}")
    public String redirectOldCreate(@PathVariable("courseID") String courseID) {
        return "redirect:/announcement/list?courseID=" + courseID;
    }
}