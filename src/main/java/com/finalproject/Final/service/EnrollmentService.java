package com.finalproject.Final.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.Final.dto.EnrollmentDTO;
import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.model.EnrollmentBean;
import com.finalproject.Final.model.EnrollmentStatisticsBean;
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
	
	@Autowired
	private ScholarshipDiscountService scholarshipDiscountService;

//	public String createEnrollment(String userId, String courseId) {
//
//		   // Prevent duplicate enrollment
//	    EnrollmentBean existing =
//	            repo.findByUserAndCourse(userId, courseId);
//
//	    if (existing != null) {
//	    	  throw new RuntimeException("You are already enrolled in this course.");
//	    }
//	    
//	    // Check if the course is already full
//	    if (courseService.getSeatsAvailable(courseId) <= 0) {
//	        throw new RuntimeException("Course is full.");
//	    }
//
//	    CourseBean course = cRepo.findById(courseId);
//	    
//	    Double originalFee = course.getFee();
//	    
////	    Double discount =
////	            scholarshipDiscountService
////	            .getApprovedDiscount(userId, courseId);
//	    
////	    Double finalFee =
////	            originalFee - discount;
//	    
////	    if(finalFee < 0){
////	        finalFee = 0.0;
////	    }
//
////	    Double fee = course.getFee();
////	    String enrollmentId = repo.save(
////	            userId,
////	            courseId,
////	            LocalDate.now(),
////	            originalFee,
////	            discount,
////	            finalFee
////	    );
//	    
//	    Double fee = course.getFee();
//
//
//	 // calculate scholarship discount
//	 Double discountAmount =
//	         scholarshipDiscountService.getDiscountAmount(
//	                 userId,
//	                 courseId,
//	                 fee
//	         );
//
//
//	 // final amount after discount
//	 Double finalFee =
//	         fee - discountAmount;
//
//
//
//	 String enrollmentId = repo.save(
//	         userId,
//	         courseId,
//	         LocalDate.now(),
//	         fee,
//	         discountAmount,
//	         finalFee
//	 );
//
//	    courseService.decreaseSeat(courseId);
//
//	    return enrollmentId;
//
////	    return repo.save(
////	            userId,
////	            courseId,
////	            LocalDate.now(),
////	            fee,
////	            fee
//	    
//	}
	
	
	public String createEnrollment(String userId, String courseId) {

	    // =====================================
	    // 1. Prevent duplicate enrollment
	    // =====================================

	    EnrollmentBean existing =
	            repo.findByUserAndCourse(userId, courseId);

	    if (existing != null) {
	        throw new RuntimeException(
	                "You are already enrolled in this course."
	        );
	    }



	    // =====================================
	    // 2. Check course seat availability
	    // =====================================

	    if (courseService.getSeatsAvailable(courseId) <= 0) {

	        throw new RuntimeException(
	                "Course is full."
	        );
	    }



	    // =====================================
	    // 3. Get course information
	    // =====================================

	    CourseBean course =
	            cRepo.findById(courseId);


	    Double originalFee =
	            course.getFee();



	    // =====================================
	    // 4. Calculate scholarship discount
	    // =====================================

	    Double discountAmount =
	            scholarshipDiscountService.getDiscountAmount(
	                    userId,
	                    courseId,
	                    originalFee
	            );


	    // Prevent negative final fee

	    if (discountAmount == null) {

	        discountAmount = 0.0;

	    }


	    Double finalFee =
	            originalFee - discountAmount;


	    if (finalFee < 0) {

	        finalFee = 0.0;

	    }



	    // =====================================
	    // 5. Create enrollment
	    // =====================================

//	    String enrollmentId =
//	            repo.save(
//	                    userId,
//	                    courseId,
//	                    LocalDate.now(),
//	                    originalFee,
//	                    discountAmount,
//	                    finalFee
//	            );
	    
	    String scholarshipApplicationId =
	            scholarshipDiscountService
	            .getApprovedScholarshipApplicationId(
	                    userId,
	                    courseId
	            );
	    
	    //test
	    System.out.println(
	    	    "Scholarship Application ID: "
	    	    + scholarshipApplicationId
	    	);


	    String enrollmentId =
	            repo.save(
	                    userId,
	                    courseId,
	                    LocalDate.now(),
	                    originalFee,
	                    discountAmount,
	                    finalFee,
	                    scholarshipApplicationId
	            );



	    // =====================================
	    // 6. Reduce available seat
	    // =====================================

	    courseService.decreaseSeat(courseId);



	    // =====================================
	    // 7. Return enrollment ID
	    // =====================================
	    
	    //test
	    Double discountAmount1 =
	            scholarshipDiscountService.getDiscountAmount(
	                    userId,
	                    courseId,
	                    originalFee
	            );


	    System.out.println("Original Fee: " + originalFee);
	    System.out.println("Discount: " + discountAmount1);
	    System.out.println("Final Fee: " + finalFee);

	    return enrollmentId;
	}

	public EnrollmentBean getById(String id) {

		return repo.findById(id);
	}

//	public void confirmEnrollment(String id) {
//
//		repo.updateStatus(id, "Active");
//
//	}
	
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
	
	public List<CourseBean> searchMyCourses(
	        String userId,
	        String keyword,
	        String categoryId
	){

	    return repo.searchMyCourses(
	            userId,
	            keyword,
	            categoryId
	    );

	}

	
	public int countEnrolledStudents(String courseId) {

	    return repo.countEnrolledStudents(courseId);

	}
	
	public List<EnrollmentBean> getAllEnrollments(){

	    return repo.getAllEnrollments();

	}
	
	public EnrollmentStatisticsBean getEnrollmentStatistics() {

	    return repo.getEnrollmentStatistics();

	}
	
	
	public void updateEnrollmentStatus(
	        String enrollmentId,
	        String status, 
	        String reason
	){

	    repo.updateStatus(
	            enrollmentId,
	            status, 
	            reason
	    );

	}
	
	
	public List<EnrollmentBean> searchEnrollments(
	        String keyword,
	        String status,
	        String paymentStatus
	){

	    return repo.searchEnrollments(
	            keyword,
	            status,
	            paymentStatus
	    );

	}
}