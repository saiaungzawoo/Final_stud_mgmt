package com.finalproject.Final.controller;

import org.springframework.beans.factory.annotation.Autowired;

//import java.awt.image.BufferedImage;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;

//import javax.imageio.ImageIO;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.finalproject.Final.model.AnnouncementBean;
import com.finalproject.Final.model.TeacherBean;
import com.finalproject.Final.model.UserBean;
import com.finalproject.Final.repository.AnnouncementRepository;
import com.finalproject.Final.repository.TeacherRepository;
import com.finalproject.Final.repository.UserRepository;
import com.finalproject.Final.service.AnnouncementService;

import jakarta.servlet.http.HttpSession;

//import com.springboot.Repository.UsersRepository;

//import com.springboot.model.UserBeans;
//import com.springboot.model.uploadBean;

import jakarta.validation.Valid;
//import com.springboot.repository.StudentRepository;
//import com.springboot.repository.UserRepository;





@Controller
@RequestMapping("/announce/announcement")
public class AnnouncementController {
	  @Autowired
	  private  AnnouncementService announcementService;
	    


    private final AnnouncementRepository announcementRepository;

    public AnnouncementController(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }
  
    
   

  


    @GetMapping
    public String index(Model model) {
        String teacherId = getLoginTeacherId();

        model.addAttribute("announcements", announcementRepository.findByTeacherId(teacherId));
        model.addAttribute("announcement", new AnnouncementBean());

        return "teacher/teacher-announcement";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute AnnouncementBean announcement) {
        String teacherId = getLoginTeacherId();
       
        announcement.setCreatedByID(teacherId);

        if (announcement.getPriority() == null || announcement.getPriority().isBlank()) {
            announcement.setPriority("Normal");
        }

        if (announcement.getTargetType() == null || announcement.getTargetType().isBlank()) {
            announcement.setTargetType("ALL_STUDENTS");
        }
        announcement.getCourseID();
        
        announcementRepository.save(announcement);

        return "redirect:/announce/announcement";
    }

	/*
	 * @GetMapping("/{id}/edit") public String detail(@PathVariable String id, Model
	 * model) { String teacherId = getLoginTeacherId();
	 * 
	 * model.addAttribute("announcement",
	 * announcementRepository.findByIdAndTeacherId(id, teacherId));
	 * 
	 * return "teacher/teacher-announcement-detail"; }
	 */
    @PostMapping("/update")
    public String updateAnnouncement(AnnouncementBean announcement){

        announcementService.update(announcement);

        return "redirect:/announce/announcement";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable String id) {
        String teacherId = getLoginTeacherId();

        announcementRepository.deleteByIdAndTeacherId(id, teacherId);

        return "redirect:/teacher/announcement";
    }

    private String getLoginTeacherId(){

        return "00ee5b4b-7a6f-11f1-8f4f-183d2d227d02";

    }
    @GetMapping("/{id}")
    public String announcementDetail(
            @PathVariable String id,
            Model model) {

        AnnouncementBean announcement =
                announcementRepository.getAnnouncementById(id);

        model.addAttribute("announcement", announcement);

        return "teacher/announcement-detail";
    }
}

