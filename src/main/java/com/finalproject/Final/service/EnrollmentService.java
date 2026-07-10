package com.finalproject.Final.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.Final.dto.EnrollmentDTO;
import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.model.EnrollmentBean;
import com.finalproject.Final.repository.EnrollmentRepository;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository repo;


    // CREATE ENROLLMENT
    public String createEnrollment(EnrollmentDTO dto) {

        // prevent duplicate enrollment
        if (repo.existsByUserIdAndCourseId(
                dto.getUserId(),
                dto.getCourseId())) {

            throw new RuntimeException(
                "Already enrolled in this course"
            );
        }


        return repo.save(
                dto.getUserId(),
                dto.getCourseId(),
                LocalDate.now()
        );
    }


    // GET BY ENROLLMENT ID
    public EnrollmentBean getById(String enrollmentId) {

        return repo.findById(enrollmentId);
    }



    // GET USER ENROLLMENTS
    public List<EnrollmentBean> getByUser(String userId) {

        return repo.findByUser(userId);
    }



    // CONFIRM PAYMENT -> ACTIVE ENROLLMENT
    public void confirmEnrollment(String enrollmentId) {

        repo.updateStatus(
                enrollmentId,
                "Active"
        );
    }



    // GET COURSES USER ENROLLED IN
    public List<CourseBean> getEnrolledCourses(String userId) {

        return repo.getEnrolledCourses(userId);
    }

}