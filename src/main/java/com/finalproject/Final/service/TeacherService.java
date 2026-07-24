package com.finalproject.Final.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.Final.model.TeacherBean;
import com.finalproject.Final.repository.TeacherRepository;


//sai
//for course create
@Service
public class TeacherService {
	
	
	 @Autowired
	    private TeacherRepository teacherRepository;



	    public List<TeacherBean> getAllTeachers() {

	        return teacherRepository.getAllTeacher();

	    }


}
