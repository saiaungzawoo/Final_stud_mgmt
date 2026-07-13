package com.finalproject.Final.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.Final.repository.PaymentRepository;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    // MAIN FLOW: called when user clicks "Pay"
    public void processPayment(int enrollmentId,
                               int userId,
                               int amount,
                               String paymentMethod,
                               String paymentType,
                               int courseId) {

<<<<<<< Updated upstream
        // create payment
        int paymentId = paymentRepository.savePayment(
                amount,
                paymentMethod,
                "PENDING",
                courseId
        );

        // create payment record (user link)
        paymentRepository.savePaymentRecord(
                paymentId,
                userId,
                paymentType
        );

        // update enrollment status
        enrollmentService.markAsPaid(enrollmentId);
=======
	/*
	 * public void processPayment(PaymentDTO dto) {
	 * 
	 * String transactionReference = "TXN-" +
	 * LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "-" +
	 * UUID.randomUUID().toString().substring(0, 8).toUpperCase();
	 */
        // 1. get enrollment 
      

       /* // 2. prevent double payment
        if (paymentRepository.existsPaidPayment(dto.getEnrollmentId())) {
            throw new RuntimeException("Already paid for this enrollment");
        }*/

		/*
		 * // 3. check seat availability BEFORE payment int seats =
		 * courseRepository.getSeatsAvailable(courseId); if (seats <= 0) { throw new
		 * RuntimeException("No seats available"); }
		 */
		/*
		 * // 4. save payment int paymentId = paymentRepository.savePayment(
		 * transactionReference, dto.getAmount(), dto.getPaymentMethod(), "SUCCESS",
		 * courseId, dto.getEnrollmentId() );
		 */

		/*
		 * // 5. save payment record paymentRepository.savePaymentRecord( paymentId,
		 * userId, dto.getPaymentType() );
		 */
		/*
		 * // 6. confirm enrollment
		 * enrollmentService.confirmEnrollment(dto.getEnrollmentId());
		 * 
		 * // 7. reduce seat courseRepository.decreaseSeat(courseId); }
		 */
    public PaymentBean getByEnrollmentId(int enrollmentId) {
        return paymentRepository.getByEnrollmentId(enrollmentId);
    }
    
    public PaymentBean getById(int id) {
        return paymentRepository.getById(id);
    }
    
    public void markReceiptDownloaded(int paymentId) {
        paymentRepository.markReceiptDownloaded(paymentId);
>>>>>>> Stashed changes
    }

    // get payment details for UI page
    public Map<String, Object> getPaymentByEnrollment(int enrollmentId) {
        return paymentRepository.getPaymentByEnrollment(enrollmentId);
    }
}