package com.finalproject.Final.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.finalproject.Final.model.TeacherBean;
import com.finalproject.Final.repository.TeacherRepository;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/users")
public class TeacherController {
	@Autowired
	private TeacherRepository  mRepo;
	
	@GetMapping("/forms")
	public ModelAndView showForm() {
		
		return new ModelAndView("teacher/create-teacher","userObj",new TeacherBean());
		
	}
	@PostMapping("/teacherRegister")
	public String teacherRegister(@Valid @ModelAttribute("userObj") TeacherBean obj,   BindingResult result,  MultipartFile file,Model m) throws IllegalStateException, IOException {


        if(file.isEmpty()) {

            m.addAttribute("fileError",
                    "Please select image");

        }


        if(result.hasErrors() || file.isEmpty()) {

            return "teacher/create-teacher";
        }



        long maxSize = 2 * 1024 * 1024;


        if(file.getSize() > maxSize) {

            m.addAttribute("fileError",
                    "File size must be less than 2MB");

            return "teacher/create-teacher";
        }



        String contentType = file.getContentType();

        if(contentType == null ||
                !contentType.startsWith("image/")) {


            m.addAttribute("fileError",
                    "Only image files allowed");

            return "teacher/create-teacher";
        }



        BufferedImage image =
                ImageIO.read(file.getInputStream());


        if(image == null) {

            m.addAttribute("fileError",
                    "Invalid image file");

            return "teacher/create-teacher";
        }



        String fileName = file.getOriginalFilename();


        file.transferTo(
                Paths.get("uploads/" + fileName)
        );



        obj.setProfileImage(
                "uploads/" + fileName
        );



        // UUID role ID ထည့်
        obj.setUserID(UUID.randomUUID().toString());

        obj.setRoleID("00ec67a1-7a6f-11f1-8f4f-183d2d227d02");

        obj.setIsActive(1);



        obj.setPassword(
                passwordEncoder.encode(
                        obj.getPassword()
                )
        );



        int i = mRepo.insertTeacher(obj);



        if(i != 0) {

            return "redirect:/users/teacherLists";

        }else {

            m.addAttribute(
                    "fail",
                    "insert fail"
            );

            return "teacher/create-teacher";
        }

    }





    @Configuration
    public class WebConfig implements WebMvcConfigurer {


        @Override
        public void addResourceHandlers(
                ResourceHandlerRegistry registry) {


            registry.addResourceHandler("/uploads/**")
                    .addResourceLocations("file:uploads/");

        }
    }





    @GetMapping("/teacherLists")
    public String showAllTeacher(Model m) {


        List<TeacherBean> list =mRepo.getAllTeacher();
        System.out.println("Teacher count = " + list.size());
        System.out.println(list);

        m.addAttribute("teacherList",list);
        return "teacher/teachers-list";
    }





    @GetMapping("/getbyid")
    public ModelAndView getById( @RequestParam("id") String userID) {
        TeacherBean obj = mRepo.getByTeacherId(userID);
        return new ModelAndView("teacher/teacher-edit","teacherObj", obj );

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



        return "redirect:/users/teacherLists";

    }

}