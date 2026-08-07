 package com.finalproject.Final.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.finalproject.Final.model.AnnouncementRecipientBean;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finalproject.Final.model.AnnouncementBean;
import com.finalproject.Final.model.UserBean;
import com.finalproject.Final.repository.AnnouncementRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import tools.jackson.databind.ObjectMapper;

import com.finalproject.Final.repository.AnnouncementRecipientRepository;
@Controller
@RequestMapping("/announcement")
public class AnnouncementController {

    private final AnnouncementRepository announcementRepo;
    private final AnnouncementRecipientRepository recipientRepo;
   

    public AnnouncementController(
            AnnouncementRepository announcementRepo,
            AnnouncementRecipientRepository recipientRepo) {

        this.announcementRepo = announcementRepo;
        this.recipientRepo = recipientRepo;
    }

	/*
	 * / ၁။ အဓိက Dashboard Page (Course list ပြခြင်းနှင့် ရွေးချယ်ထားသော Course ၏
	 * Announcement list တွဲပြခြင်း) URL: /announcement/list
	 */
    @GetMapping("/list")
    public String announcementDashboard(
            @RequestParam(value = "courseID", required = false) String courseID,
            Model model ,HttpSession session) {
      
        UserBean loginUser = (UserBean) session.getAttribute("loginUser");

          String teacherID = loginUser.getUserID();

        //  Course List Grid Card
        model.addAttribute("courseList", announcementRepo.getTeacherCourses(teacherID));

        // Form Object Binding 
        AnnouncementBean bean = new AnnouncementBean();
        if (courseID != null && !courseID.isEmpty()) {
            bean.setCourseID(courseID); // Course  Modal  CourseID  Default 
            
            //  Course  Announcement List 
            List<AnnouncementBean> announcementList = announcementRepo.getAnnouncementsByCourse(courseID);
            model.addAttribute("announcementList", announcementList);
            System.out.println("ANNOUNCEMENT SIZE = " 
                    + announcementList.size());

            model.addAttribute("courseID", courseID);
        }

        model.addAttribute("announcement", bean);

        return "teacher/announcement-dashboard"; 
    }

	/*
	 * ၂။ Announcement အသစ်ဆောက်ခြင်းနှင့် တည်းဖြတ်ခြင်း (Save / Update Endpoint)
	 * URL: /announcement/save
	 */
//    annoucneementController sav e method
   @PostMapping("/save")
        public String saveAnnouncement(@Valid @ModelAttribute("announcement") AnnouncementBean bean, BindingResult result,HttpSession session ,Model model) {
          
          if(result.hasErrors()) {

              UserBean loginUser =
                      (UserBean) session.getAttribute("loginUser");

              model.addAttribute("courseList",
                      announcementRepo.getTeacherCourses(
                              loginUser.getUserID()
                      ));

              model.addAttribute("openModal", true);

              return "teacher/announcement-dashboard";
          }
            UserBean loginUser = (UserBean) session.getAttribute("loginUser");

            String teacherID = loginUser.getUserID();
            bean.setCreatedByID(teacherID);
           
          
            if (bean.getAnnouncementID() == null || bean.getAnnouncementID().isEmpty()) {

                bean.setAnnouncementID(java.util.UUID.randomUUID().toString());

                bean.setPublishDate(LocalDateTime.now());

                // 1. Save announcement
                announcementRepo.saveAnnouncement(bean);


                // 2. Find target users
                List<String> userIDs = null;


                switch(bean.getTargetType()) {

                    case "ALL":
                        userIDs = recipientRepo.getAllUserIDs();
                        break;


                    case "ALL_STUDENTS":
                        userIDs = recipientRepo.getStudentIDs();
                        break;


                    case "ALL_TEACHERS":
                        userIDs = recipientRepo.getTeacherIDs();
                        break;
                }


                // 3. Save recipients
                if(userIDs != null) {

                    for(String userID : userIDs) {
                         System.out.println("RECIPIENT USER = " + userID);
                        AnnouncementRecipientBean recipient =
                                new AnnouncementRecipientBean();

                        recipient.setAnnouncementID(
                                bean.getAnnouncementID()
                        );

                        recipient.setUserID(userID);


                        recipientRepo.saveRecipient(recipient);
                    }
                }


            }
            else {
               AnnouncementBean old =
                          announcementRepo.findById(bean.getAnnouncementID());

                  if(old.getExpiryDate() != null &&
                     old.getExpiryDate().isBefore(LocalDateTime.now())) {

                      return "redirect:/announcement/list?courseID=" 
                              + bean.getCourseID();
                  }

                // 1. Update announcement
                announcementRepo.updateAnnouncement(bean);

                // 2. Delete old recipients
                recipientRepo.deleteByAnnouncementID(bean.getAnnouncementID());

                // 3. Get new recipients
                List<String> userIDs = null;

                switch (bean.getTargetType()) {

                    case "ALL":
                        userIDs = recipientRepo.getAllUserIDs();
                        break;

                    case "ALL_STUDENTS":
                        userIDs = recipientRepo.getStudentIDs();
                        break;

                    case "ALL_TEACHERS":
                        userIDs = recipientRepo.getTeacherIDs();
                        break;
                }

                // 4. Insert new recipients
                if (userIDs != null) {

                    for (String userID : userIDs) {

                        AnnouncementRecipientBean recipient =
                                new AnnouncementRecipientBean();

                        recipient.setAnnouncementID(bean.getAnnouncementID());
                        recipient.setUserID(userID);

                        recipientRepo.saveRecipient(recipient);
                    }
                }
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
    @PostMapping("/delete")
    public String deleteAnnouncement(
            @RequestParam String announcementID,
            @RequestParam String courseID) {

        // 1. Delete recipients
        recipientRepo.deleteByAnnouncementID(announcementID);

        // 2. Delete announcement
        announcementRepo.deleteAnnouncement(announcementID);

        return "redirect:/announcement/list?courseID=" + courseID;
    }
    @GetMapping("/status/{announcementID}")
    public String announcementStatus(
            @PathVariable String announcementID,
            Model model) {


        List<AnnouncementRecipientBean> recipientList =
                recipientRepo.getRecipientStatus(announcementID);


        model.addAttribute(
                "recipientList",
                recipientList
        );

        model.addAttribute(
                "announcementID",
                announcementID
        );


        return "teacher/announcement-status";
    }
    @GetMapping("/status/data/{id}")
    @ResponseBody
    public String getStatusData(@PathVariable String id) throws Exception {

        List<AnnouncementRecipientBean> list =
                recipientRepo.getRecipientStatus(id);

        List<Map<String,Object>> result = new ArrayList<>();

        for(AnnouncementRecipientBean r : list){

            Map<String,Object> map = new HashMap<>();

            map.put("userName", r.getUserName());
            map.put("read", r.isRead());
            map.put("acknowledged", r.isAcknowledged());

            result.add(map);
        }

        return new ObjectMapper().writeValueAsString(result);
    }
    
    
    //TZM
    @GetMapping("/student")
    public String studentAnnouncement(Model model) {

        model.addAttribute("announcements",
        		announcementRepo.getStudentAnnouncements());

        return "student/student-announcement";
    }
    @GetMapping("/student/{id}")
    public String studentAnnouncementDetail(@PathVariable String id,
                                            Model model,
                                            HttpSession session) {

        UserBean loginUser = (UserBean) session.getAttribute("loginUser");
      /*System.out.println("Announcement ID = " + id);
        System.out.println("User ID = " + loginUser.getUserID());*/
        if (loginUser != null) {
        	recipientRepo.markAsRead(
                    id,
                    loginUser.getUserID()
            );
        }

        AnnouncementBean announcement =
        		announcementRepo.getAnnouncementById(id);

        model.addAttribute("announcement", announcement);

        return "student/student-announcement-detail";
    }
    
}