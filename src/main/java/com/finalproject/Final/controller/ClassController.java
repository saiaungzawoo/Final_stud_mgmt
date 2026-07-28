package com.finalproject.Final.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.finalproject.Final.model.ClassBean;
import com.finalproject.Final.repository.ClassRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/teacher")
public class ClassController {


    @Autowired
    private ClassRepository repository;



    @GetMapping("/classes")
    public String classes(
            HttpSession session,
            Model model
    ){


        String teacherID =
                (String) session.getAttribute("userID");


        List<ClassBean> classes =
                repository.getTeacherClasses(teacherID);



        model.addAttribute("classes", classes);


        return "teacher/class";

    }


}