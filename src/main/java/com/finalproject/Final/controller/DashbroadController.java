package com.finalproject.Final.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.finalproject.Final.model.TeacherBean;
import com.finalproject.Final.model.UserBean;
import com.finalproject.Final.repository.TeacherRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/dashboard")
public class DashbroadController {
	 @Autowired
	    private TeacherRepository mRepo;
	 @Autowired
	    private PasswordEncoder passwordEncoder;
	
	
	  @GetMapping("/getbyid1")
	    public ModelAndView getByTeacherId( @RequestParam("id") String userID) {
	        TeacherBean obj = mRepo.getByTeacherId(userID);
	        return new ModelAndView("teacher/teacher-profile","teacherObj", obj );

	    }
	  @GetMapping("/dashboard-teacher")
	  public String teacherdashboard(
	          Model model,
	          HttpSession session) {


	      UserBean loginUser =
	              (UserBean) session.getAttribute("loginUser");


	      String teacherID =
	              loginUser.getUserID();



	      model.addAttribute(
	              "classCount",
	              mRepo.countClasses(teacherID)
	      );


	      model.addAttribute(
	              "attendancePercent",
	              mRepo.todayAttendancePercent(teacherID)
	      );


	      model.addAttribute(
	              "assignmentCount",
	              mRepo.countAssignments(teacherID)
	      );


	      model.addAttribute(
	              "pendingCount",
	              mRepo.countPendingSubmission(teacherID)
	      );


	      model.addAttribute(
	              "todaySchedules",
	              mRepo.getTodaySchedule(teacherID)
	      );


	      model.addAttribute(
	              "announcements",
	              mRepo.getRecentAnnouncements(teacherID)
	      );


	      return "teacher/teacher-dashboard";
	  }

	  @PostMapping("/update")
	    public String updateUpload(
	            @Valid @ModelAttribute("teacherObj") TeacherBean obj,
	            BindingResult result,
	            @RequestParam("file") MultipartFile file,
	            Model m) throws IOException {



	        if(result.hasErrors()) {

	            return "teacher/teacher-edit";
	        }



	        TeacherBean oldObj =
	                mRepo.getByTeacherId(
	                        obj.getUserID()
	                );




	        if(file != null && !file.isEmpty()) {



	            long maxSize =
	                    2 * 1024 * 1024;



	            if(file.getSize() > maxSize) {

	                m.addAttribute(
	                    "fileError",
	                    "File size must be less than 2MB"
	                );

	                return "teacher/teacher-edit";
	            }



	            String contentType =
	                    file.getContentType();



	            if(contentType == null ||
	                    !contentType.startsWith("image/")) {


	                m.addAttribute(
	                    "fileError",
	                    "Only image files allowed"
	                );


	                return "teacher/teacher-edit";
	            }




	            if(oldObj.getProfileImage()!=null) {

	                Files.deleteIfExists(
	                    Paths.get(oldObj.getProfileImage())
	                );
	            }



	            String fileName =
	                    file.getOriginalFilename();



	            file.transferTo(
	                    Paths.get("uploads/" + fileName)
	            );



	            obj.setProfileImage(
	                    "uploads/" + fileName
	            );



	        }else {


	            obj.setProfileImage(
	                    oldObj.getProfileImage()
	            );

	        }




	        if(obj.getPassword()!=null &&
	                !obj.getPassword().isBlank()) {


	            obj.setPassword(
	                passwordEncoder.encode(
	                    obj.getPassword()
	                )
	            );

	        }else {


	            obj.setPassword(
	                oldObj.getPassword()
	            );
	        }




	        mRepo.updateUpload(obj);



	        return "redirect:/dashboard/dashboard-teacher";

	    }
	    

}
