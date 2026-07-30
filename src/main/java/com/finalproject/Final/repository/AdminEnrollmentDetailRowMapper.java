package com.finalproject.Final.repository;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.finalproject.Final.model.AdminEnrollmentDetailBean;



public class AdminEnrollmentDetailRowMapper 
implements RowMapper<AdminEnrollmentDetailBean>{



@Override
public AdminEnrollmentDetailBean mapRow(
        ResultSet rs,
        int rowNum
) throws SQLException {


    AdminEnrollmentDetailBean e =
            new AdminEnrollmentDetailBean();



    e.setEnrollmentId(
            rs.getString("enrollmentID")
    );


    if(rs.getDate("enrollment_date") != null){

        e.setEnrollmentDate(
                rs.getDate("enrollment_date")
                .toLocalDate()
        );
    }



    e.setEnrollmentStatus(
            rs.getString("status")
    );


    e.setPaymentStatus(
            rs.getString("payment_status")
    );



    /*
     * Student
     */

    e.setStudentId(
            rs.getString("userID")
    );


    e.setStudentName(
            rs.getString("student_name")
    );


    e.setStudentEmail(
            rs.getString("student_email")
    );


    e.setStudentPhone(
            rs.getString("phone_no")
    );



    /*
     * Course
     */

    e.setCourseId(
            rs.getString("courseID")
    );


    e.setCourseName(
            rs.getString("course_name")
    );


    e.setCourseFee(
            rs.getDouble("course_fee")
    );



    /*
     * Teacher
     */

    e.setTeacherName(
            rs.getString("teacher_name")
    );


    e.setTeacherEmail(
            rs.getString("teacher_email")
    );



    /*
     * Payment
     */

    e.setFinalFee(
            rs.getDouble("final_fee")
    );


    e.setTotalPaid(
            rs.getDouble("total_paid")
    );


    e.setRemainingBalance(
            rs.getDouble("remaining_balance")
    );
    
    e.setOriginalFee(
    	    rs.getDouble("original_fee")
    	);


    	e.setDiscountAmount(
    	    rs.getDouble("discount_amount")
    	);
    	
    	e.setPaymentType(
    		    rs.getString("payment_type_name")
    		);



    /*
     * Installment
     */

    e.setTotalInstallments(
            rs.getInt("total_installments")
    );


    e.setCompletedInstallments(
            rs.getInt("completed_installments")
    );
    
    if(rs.getTimestamp("created_at") != null){

        e.setCreatedAt(
            rs.getTimestamp("created_at")
            .toLocalDateTime()
        );

    }


    if(rs.getTimestamp("updated_at") != null){

        e.setUpdatedAt(
            rs.getTimestamp("updated_at")
            .toLocalDateTime()
        );

    }
    
    
    if(rs.getDate("first_payment_date") != null){

        e.setPaymentDate(
            rs.getDate("first_payment_date")
            .toLocalDate()
        );

    }
    
    e.setStatusReason(
    	    rs.getString("status_reason")
    	);



    return e;

}

}