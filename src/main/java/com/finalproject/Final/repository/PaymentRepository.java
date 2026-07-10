package com.finalproject.Final.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.PaymentBean;

@Repository
public class PaymentRepository {

    @Autowired
    private JdbcTemplate jdbc;

    // Save payment (main payment table)
    public int savePayment(String transactionReference, int amount, String method, String status, int courseId, int enrollmentId) {

        String sql = "INSERT INTO payment\r\n"
        		+ "        (transaction_reference, amount, payment_date, payment_method, payment_status, created_at, updated_at, course_id, enrollment_id)\r\n"
        		+ "        VALUES (?, ?, ?, ?, ?, NOW(), NOW(), ?, ?)";

        jdbc.update(sql, transactionReference,  amount, LocalDate.now(), method, status, courseId, enrollmentId);
 
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
    
    public PaymentBean getByEnrollmentId(int enrollmentId) {

        String sql = " SELECT *\r\n"
        		+ "        FROM payment\r\n"
        		+ "        WHERE enrollment_id = ?\r\n"
        		+ "        ORDER BY id DESC\r\n"
        		+ "        LIMIT 1";

        List<PaymentBean> list = jdbc.query(sql, new PaymentRowMapper(), enrollmentId);

        return list.isEmpty() ? null : list.get(0);
    }
    
    public PaymentBean getById(int id) {

        String sql = """
                SELECT *
                FROM payment
                WHERE id = ?
                """;

        return jdbc.queryForObject(
                sql,
                new PaymentRowMapper(),
                id);
    }
    
    public void markReceiptDownloaded(int paymentId) {

        String sql = """
                UPDATE payment
                SET receipt_downloaded = 1
                WHERE id = ?
                """;

        jdbc.update(sql, paymentId);

    }

    
}