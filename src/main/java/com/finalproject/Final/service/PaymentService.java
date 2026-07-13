/*
 * package com.finalproject.Final.service;
 * 
 * 
 * import java.time.LocalDate; import java.time.format.DateTimeFormatter; import
 * java.util.UUID;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.stereotype.Service;
 * 
 * import com.finalproject.Final.dto.PaymentDTO; import
 * com.finalproject.Final.model.EnrollmentBean; import
 * com.finalproject.Final.model.PaymentBean; import
 * com.finalproject.Final.repository.CourseRepository; import
 * com.finalproject.Final.repository.PaymentRepository;
 * 
 * @Service public class PaymentService {
 * 
 * @Autowired private PaymentRepository paymentRepository;
 * 
 * @Autowired private EnrollmentService enrollmentService;
 * 
 * @Autowired private CourseRepository courseRepository;
 * 
 * public void processPayment(PaymentDTO dto) {
 * 
 * String transactionReference = "TXN-" +
 * LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "-" +
 * UUID.randomUUID().toString().substring(0, 8).toUpperCase();
 * 
 * // 1. get enrollment EnrollmentBean enrollment =
 * enrollmentService.getById("dto.getEnrollmentId()");
 * 
 * 
 * String courseId = enrollment.getCourseId(); String userId =
 * enrollment.getUserId();
 * 
 * 
 * // 2. prevent double payment if
 * (paymentRepository.existsPaidPayment(dto.getEnrollmentId())) { throw new
 * RuntimeException("Already paid for this enrollment"); }
 * 
 * // 3. check seat availability BEFORE payment int seats =
 * courseRepository.getSeatsAvailable(courseId); if (seats <= 0) { throw new
 * RuntimeException("No seats available"); }
 * 
 * // 4. save payment int paymentId = paymentRepository.savePayment(
 * transactionReference, dto.getAmount(), dto.getPaymentMethod(), "SUCCESS",
 * courseId, dto.getEnrollmentId() );
 * 
 * // 5. save payment record paymentRepository.savePaymentRecord( paymentId,
 * userId, dto.getPaymentType() );
 * 
 * // 6. confirm enrollment
 * enrollmentService.confirmEnrollment(dto.getEnrollmentId());
 * 
 * // 7. reduce seat courseRepository.decreaseSeat(courseId); }
 * 
 * public PaymentBean getByEnrollmentId(int enrollmentId) { return
 * paymentRepository.getByEnrollmentId(enrollmentId); }
 * 
 * public PaymentBean getById(int id) { return paymentRepository.getById(id); }
 * 
 * public void markReceiptDownloaded(int paymentId) {
 * paymentRepository.markReceiptDownloaded(paymentId); }
 * 
 * }
 */