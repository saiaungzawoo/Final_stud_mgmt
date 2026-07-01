package com.finalproject.Final.repository;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepository {

    @Autowired
    private JdbcTemplate jdbc;

    // Save payment (main payment table)
    public int savePayment(int amount, String method, String status, int courseId, int enrollmentId) {

        String sql = "INSERT INTO payment\r\n"
        		+ "        (amount, payment_date, payment_method, payment_status, created_at, updated_at, course_id, enrollment_id)\r\n"
        		+ "        VALUES (?, ?, ?, ?, NOW(), NOW(), ?, ?)";

        jdbc.update(sql, amount, LocalDate.now(), method, status, courseId, enrollmentId);

        return jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    }

    //  Save payment record (link user ↔ payment)
    public void savePaymentRecord(int paymentId,
                                  int userId,
                                  String paymentType) {

        String sql = "INSERT INTO payment_record\r\n"
        		+ "            (payment_id, user_id, payment_type)\r\n"
        		+ "            VALUES (?, ?, ?)";

        jdbc.update(sql, paymentId, userId, paymentType);
    }
    
    public boolean existsPaidPayment(int enrollmentId) {

        String sql = " SELECT COUNT(*) \r\n"
        		+ "        FROM payment \r\n"
        		+ "        WHERE enrollment_id = ? AND payment_status = 'PAID'";

        Integer count = jdbc.queryForObject(sql, Integer.class, enrollmentId);
        return count != null && count > 0;
    }

    // Get payment by enrollment
//    public Map<String, Object> getPaymentByEnrollment(int enrollmentId) {
//
//        String sql = "SELECT p.*\r\n"
//        		+ "            FROM payment p\r\n"
//        		+ "            JOIN enrollment e ON p.course_id = e.course_id\r\n"
//        		+ "            WHERE e.id = ?";
//
//        return jdbc.queryForMap(sql, enrollmentId);
//    }
}