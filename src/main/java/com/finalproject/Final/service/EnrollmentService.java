package com.finalproject.Final.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.Final.dto.EnrollmentDTO;
import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.model.EnrollmentBean;
import com.finalproject.Final.repository.CourseRepository;
import com.finalproject.Final.repository.EnrollmentRepository;

@Service
public class EnrollmentService {

	@Autowired
	private EnrollmentRepository repo;

	@Autowired
	private CourseRepository cRepo;

	public String createEnrollment(String userId, String courseId) {

	    EnrollmentBean existing =
	            repo.findByUserAndCourse(userId, courseId);

	    if (existing != null) {
	    	  throw new RuntimeException("You are already enrolled in this course.");
	    }

	    CourseBean course = cRepo.findById(courseId);

	    Double fee = course.getFee();

	    return repo.save(
	            userId,
	            courseId,
	            LocalDate.now(),
	            fee,
	            fee
	    );
	}

	public EnrollmentBean getById(String id) {

		return repo.findById(id);
	}

	public void confirmEnrollment(String id) {

		repo.updateStatus(id, "Active");

	}
	
	public void updateInstallmentRule(
	        String enrollmentId,
	        String installmentRuleId
	){

	    repo.updateInstallmentRule(
	            enrollmentId,
	            installmentRuleId
	    );

	}

}