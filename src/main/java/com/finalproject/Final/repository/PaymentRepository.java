package com.finalproject.Final.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.PaymentBean;

@Repository
public class PaymentRepository {

    @Autowired
    private JdbcTemplate jdbc;

    // Save Payment
    public String savePayment(
            String enrollmentId,
            String paymentMethodId,
            Double amount
    ){

    String paymentId =
    UUID.randomUUID().toString();


    String reference =
    "TXN-"+UUID.randomUUID()
    .toString()
    .substring(0,8)
    .toUpperCase();



    String sql ="INSERT INTO payment\r\n"
    		+ "    (\r\n"
    		+ "    paymentID,\r\n"
    		+ "    enrollmentID,\r\n"
    		+ "    paymentMethodID,\r\n"
    		+ "    amount,\r\n"
    		+ "    payment_date,\r\n"
    		+ "    transaction_reference,\r\n"
    		+ "    status,\r\n"
    		+ "    created_at,\r\n"
    		+ "    updated_at\r\n"
    		+ "    )\r\n"
    		+ "    VALUES\r\n"
    		+ "    (?,?,?,?,CURRENT_DATE,?,'Success',NOW(),NOW())";
   


    jdbc.update(
    sql,
    paymentId,
    enrollmentId,
    paymentMethodId,
    amount,
    reference
    );


    return paymentId;

    }
    
    //overload
    public String savePayment(
            String enrollmentId,
            String installmentPlanId,
            String paymentMethodId,
            Double amount
    ){

        String paymentId =
                UUID.randomUUID().toString();

        String reference =
                "TXN-"
                + UUID.randomUUID()
                        .toString()
                        .substring(0,8)
                        .toUpperCase();

        String sql = """
            INSERT INTO payment
            (
                paymentID,
                enrollmentID,
                installmentPlanID,
                paymentMethodID,
                amount,
                payment_date,
                transaction_reference,
                status,
                created_at,
                updated_at
            )
            VALUES
            (
                ?,?,?,?,?,CURRENT_DATE,
                ?,
                'Success',
                NOW(),
                NOW()
            )
            """;

        jdbc.update(
                sql,
                paymentId,
                enrollmentId,
                installmentPlanId,
                paymentMethodId,
                amount,
                reference
        );

        return paymentId;

    }

    // Find latest payment by enrollment
    public PaymentBean getByEnrollmentId(String enrollmentID) {

        String sql = "SELECT\r\n"
        		+ "            p.*,\r\n"
        		+ "            pm.name AS paymentMethodName,\r\n"
        		+ "            pt.name AS paymentTypeName,\r\n"
        		+ "            u.name AS studentName\r\n"
        		+ "        FROM payment p\r\n"
        		+ "       LEFT JOIN payment_method pm\r\n"
        		+ "            ON p.paymentMethodID = pm.paymentMethodID\r\n"
        		+ "       LEFT JOIN enrollment e\r\n"
        		+ "            ON p.enrollmentID = e.enrollmentID\r\n"
        		+ "       LEFT JOIN payment_type pt\r\n"
        		+ "            ON e.paymentTypeID = pt.paymentTypeID\r\n"
        		+ "       LEFT JOIN user u\r\n"
        		+ "            ON e.userID = u.userID\r\n"
        		+ "        WHERE p.enrollmentID = ?\r\n"
        		+ "        ORDER BY p.created_at DESC\r\n"
        		+ "        LIMIT 1";
            
           

        List<PaymentBean> list =
                jdbc.query(sql, new PaymentRowMapper(), enrollmentID);

        return list.isEmpty() ? null : list.get(0);
    }

    // Find payment by UUID
    public PaymentBean getById(String paymentID) {

        String sql = "SELECT\r\n"
        		+ "            p.*,\r\n"
        		+ "            pm.name AS paymentMethodName,\r\n"
        		+ "            pt.name AS paymentTypeName,\r\n"
        		+ "            u.name AS studentName\r\n"
        		+ "        FROM payment p\r\n"
        		+ "        LEFT JOIN payment_method pm\r\n"
        		+ "            ON p.paymentMethodID = pm.paymentMethodID\r\n"
        		+ "        LEFT JOIN enrollment e\r\n"
        		+ "            ON p.enrollmentID = e.enrollmentID\r\n"
        		+ "        LEFT JOIN payment_type pt\r\n"
        		+ "            ON e.paymentTypeID = pt.paymentTypeID\r\n"
        		+ "       LEFT JOIN user u\r\n"
        		+ "            ON e.userID = u.userID\r\n"
        		+ "        WHERE p.paymentID = ?";

        List<PaymentBean> list =
                jdbc.query(sql, new PaymentRowMapper(), paymentID);

        return list.isEmpty() ? null : list.get(0);
    }

}