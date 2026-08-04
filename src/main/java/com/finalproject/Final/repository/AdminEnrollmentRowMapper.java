package com.finalproject.Final.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.finalproject.Final.model.EnrollmentBean;

public class AdminEnrollmentRowMapper implements RowMapper<EnrollmentBean> {


    @Override
    public EnrollmentBean mapRow(ResultSet rs, int rowNum)
            throws SQLException {


        EnrollmentBean e = new EnrollmentBean();


        e.setEnrollmentId(
                rs.getString("enrollmentID")
        );


        e.setUserId(
                rs.getString("userID")
        );


        e.setCourseId(
                rs.getString("courseID")
        );


        e.setPaymentTypeId(
                rs.getString("paymentTypeID")
        );


//        e.setEnrollmentDate(
//                rs.getDate("enrollment_date")
//                .toLocalDate()
//        );
        
        if(rs.getDate("enrollment_date") != null){

            e.setEnrollmentDate(
                    rs.getDate("enrollment_date")
                    .toLocalDate()
            );

        }


        e.setOriginalFee(
                rs.getDouble("original_fee")
        );


        e.setDiscountedAmount(
                rs.getDouble("discount_amount")
        );


        e.setFinalFee(
                rs.getDouble("final_fee")
        );


        e.setPaymentStatus(
                rs.getString("payment_status")
        );


        e.setStatus(
                rs.getString("status")
        );



        /*
         * JOIN FIELDS
         */


        e.setUsername(
                rs.getString("user_name")
        );
        
        try {

            e.setEmail(
                    rs.getString("user_email")
            );

        } catch(SQLException ignored) {}


        e.setCourseTitle(
                rs.getString("course_title")
        );



        /*
         * PAYMENT SUMMARY
         */


        e.setTotalPaid(
                rs.getDouble("total_paid")
        );


        e.setRemainingBalance(
                rs.getDouble("remaining_balance")
        );
        
        


        /*
         * INSTALLMENT SUMMARY
         */


        e.setTotalInstallments(
                rs.getInt("total_installments")
        );


        e.setCompletedInstallments(
                rs.getInt("completed_installments")
        );

        
        


        return e;
    }

}