package com.finalproject.Final.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.Final.model.CourseCategoryBean;
import com.finalproject.Final.repository.CourseCategoryRepository;



@Service
public class CourseCategoryService {

    @Autowired
    private CourseCategoryRepository repository;

//    public List<CourseCategoryBean> getAllCategories(){
//        return repository.findAll();
//    }
    
    public List<CourseCategoryBean> getAllCategories() {

        List<CourseCategoryBean> categories = repository.findAll();

        for (CourseCategoryBean c : categories) {
            c.setCourseCount(
                repository.countCoursesByCategory(c.getCourseCategoryId())
            );
        }

        return categories;
    }

}