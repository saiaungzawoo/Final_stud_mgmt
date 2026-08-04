package com.finalproject.Final.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.AdminEnrollmentDetailBean;

@Repository
public class AdminEnrollmentDetailRepository {

	@Autowired
	private JdbcTemplate jdbc;




	    



	    public AdminEnrollmentDetailBean findById(
	            String enrollmentId
	    ) {



	        String sql = """

	        SELECT

	            e.*,
	            
	            MIN(p.payment_date) AS first_payment_date,


	            u.name AS student_name,
	            u.email AS student_email,
	            u.phone_no,


	            c.name AS course_name,
	            c.fee AS course_fee,


	            t.name AS teacher_name,
	            t.email AS teacher_email,


	            pt.name AS payment_type_name,



	            COALESCE(
	                SUM(DISTINCT p.amount),
	                0
	            )
	            AS total_paid,



	            (
	                e.final_fee -
	                COALESCE(
	                    SUM(DISTINCT p.amount),
	                    0
	                )
	            )
	            AS remaining_balance,



	            COUNT(
	                DISTINCT ip.installmentPlanID
	            )
	            AS total_installments,



	            COUNT(
	                DISTINCT CASE
	                    WHEN ip.status='Paid'
	                    THEN ip.installmentPlanID
	                END
	            )
	            AS completed_installments



	        FROM enrollment e



	        JOIN `user` u
	            ON e.userID = u.userID



	        JOIN course c
	            ON e.courseID = c.courseID



	        JOIN `user` t
	            ON c.teacherID = t.userID



	        LEFT JOIN payment_type pt
	            ON e.paymentTypeID = pt.paymentTypeID



	        LEFT JOIN payment p
	            ON e.enrollmentID = p.enrollmentID
	            AND p.status='Success'



	        LEFT JOIN installment_plan ip
	            ON e.enrollmentID = ip.enrollmentID



	        WHERE e.enrollmentID = ?



	        GROUP BY e.enrollmentID

	        """;



	        return jdbc.queryForObject(
	                sql,
	                new AdminEnrollmentDetailRowMapper(),
	                enrollmentId
	        );

	    }}

