package com.finalproject.Final.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.Final.dto.PaymentDTO;
import com.finalproject.Final.model.EnrollmentBean;
import com.finalproject.Final.repository.CourseRepository;
import com.finalproject.Final.repository.PaymentRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseRepository courseRepository;

    public void processPayment(PaymentDTO dto) {

        // 1. get enrollment 
        EnrollmentBean enrollment =  enrollmentService.getById(dto.getEnrollmentId());
               

        int courseId = enrollment.getCourseId();
        int userId = enrollment.getUserId();
        

        // 2. prevent double payment
        if (paymentRepository.existsPaidPayment(dto.getEnrollmentId())) {
            throw new RuntimeException("Already paid for this enrollment");
        }

        // 3. check seat availability BEFORE payment
        int seats = courseRepository.getSeatsAvailable(courseId);
        if (seats <= 0) {
            throw new RuntimeException("No seats available");
        }

        // 4. save payment
        int paymentId = paymentRepository.savePayment(
                dto.getAmount(),
                dto.getPaymentMethod(),
                "SUCCESS",
                courseId,
                dto.getEnrollmentId()
        );

        // 5. save payment record
        paymentRepository.savePaymentRecord(
                paymentId,
                userId,
                dto.getPaymentType()
        );

        // 6. confirm enrollment
        enrollmentService.confirmEnrollment(dto.getEnrollmentId());

        // 7. reduce seat
        courseRepository.decreaseSeat(courseId);
    }

}