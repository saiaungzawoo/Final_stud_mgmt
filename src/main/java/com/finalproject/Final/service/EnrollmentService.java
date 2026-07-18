package com.finalproject.Final.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.Final.dto.EnrollmentDTO;
import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.model.EnrollmentBean;
import com.finalproject.Final.model.InstallmentPlanBean;
import com.finalproject.Final.model.PaymentTypeBean;
import com.finalproject.Final.repository.CourseRepository;
import com.finalproject.Final.repository.EnrollmentRepository;

@Service
public class EnrollmentService {

	@Autowired
	private EnrollmentRepository repo;

	@Autowired
	private CourseRepository cRepo;
	
	@Autowired
	private InstallmentPlanService installmentPlanService;

	@Autowired
	private PaymentTypeService paymentTypeService;
	
	@Autowired
	private CourseService courseService;

	public String createEnrollment(String userId, String courseId) {

		   // Prevent duplicate enrollment
	    EnrollmentBean existing =
	            repo.findByUserAndCourse(userId, courseId);

	    if (existing != null) {
	    	  throw new RuntimeException("You are already enrolled in this course.");
	    }
	    
	    // Check if the course is already full
	    if (courseService.getSeatsAvailable(courseId) <= 0) {
	        throw new RuntimeException("Course is full.");
	    }

	    CourseBean course = cRepo.findById(courseId);

	    Double fee = course.getFee();
	    String enrollmentId = repo.save(
	            userId,
	            courseId,
	            LocalDate.now(),
	            fee,
	            fee
	    );

	    courseService.decreaseSeat(courseId);

	    return enrollmentId;

//	    return repo.save(
//	            userId,
//	            courseId,
//	            LocalDate.now(),
//	            fee,
//	            fee
	    
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
	
	public List<EnrollmentBean> getByUser(String userId){

	    return repo.findByUser(userId);

	}
	
	public List<EnrollmentBean> getMyEnrollments(String userId) {

	    List<EnrollmentBean> enrollments =
	            repo.findByUser(userId);

	    for (EnrollmentBean enrollment : enrollments) {

	        PaymentTypeBean paymentType = null;

	        if (enrollment.getPaymentTypeId() != null) {

	            paymentType =
	                    paymentTypeService.getById(
	                            enrollment.getPaymentTypeId());

	        }

	        // ============================
	        // FULL PAYMENT
	        // ============================

	        if (paymentType != null &&
	                "FULL_PAYMENT".equals(paymentType.getName())) {

	            if ("Fully Paid".equals(enrollment.getPaymentStatus())) {

	                enrollment.setTotalPaid(
	                        enrollment.getFinalFee());

	                enrollment.setRemainingBalance(0.0);

	            } else {

	                enrollment.setTotalPaid(0.0);

	                enrollment.setRemainingBalance(
	                        enrollment.getFinalFee());

	            }

	            enrollment.setCompletedInstallments(0);
	            enrollment.setTotalInstallments(0);
	            enrollment.setInstallmentPlans(null);

	        }

	        // ============================
	        // INSTALLMENT
	        // ============================

	        else if (paymentType != null &&
	                "INSTALLMENT".equals(paymentType.getName())) {


	            List<InstallmentPlanBean> plans =
	                    installmentPlanService.getByEnrollmentId(
	                            enrollment.getEnrollmentId()
	                    );


	            enrollment.setInstallmentPlans(plans);


	            Double paid =
	                    installmentPlanService.getTotalPaid(
	                            enrollment.getEnrollmentId()
	                    );


	            Integer completed =
	                    installmentPlanService.getCompletedCount(
	                            enrollment.getEnrollmentId()
	                    );


	            enrollment.setTotalPaid(paid);


	            double remaining =
	                    enrollment.getFinalFee() - paid;


	            if(remaining < 0){
	                remaining = 0;
	            }

	         // If all installments are completed
	            if (completed.equals(plans.size())) {

	                enrollment.setPaymentStatus("Fully Paid");

	                enrollment.setRemainingBalance(0.0);

	            } else {

	                enrollment.setRemainingBalance(remaining);

	            }

//	            enrollment.setRemainingBalance(
//	                    remaining
//	            );


	            enrollment.setCompletedInstallments(
	                    completed
	            );


	            enrollment.setTotalInstallments(
	                    plans.size()
	            );

	        
	            
//	            enrollment.setInstallmentPlans(plans);

	        }

	        // ============================
	        // Not chosen payment type yet
	        // ============================

	        else {

	            enrollment.setTotalPaid(0.0);

	            enrollment.setRemainingBalance(
	                    enrollment.getFinalFee());

	            enrollment.setCompletedInstallments(0);

	            enrollment.setTotalInstallments(0);

	        }

	    }

	    return enrollments;

	}
	
	public List<CourseBean> getEnrolledCourses(String userId){

	    return repo.getEnrolledCourses(userId);

	}

}