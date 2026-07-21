package com.finalproject.Final.repository;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.dto.AdminPaymentDTO;


@Repository
public class AdminPaymentRepository {


    @Autowired
    private JdbcTemplate jdbc;



    public List<AdminPaymentDTO> getRecentPayments() {


        String sql = """

                SELECT

                u.name AS studentName,

                c.name AS courseName,

                pt.name AS paymentType,

                p.amount,

                p.status,

                p.payment_date


                FROM payment p


                JOIN enrollment e

                ON p.enrollmentID = e.enrollmentID


                JOIN user u

                ON e.userID = u.userID


                JOIN course c

                ON e.courseID = c.courseID


                LEFT JOIN payment_type pt

                ON e.paymentTypeID = pt.paymentTypeID


                ORDER BY p.created_at DESC


                LIMIT 5

                """;


        return jdbc.query(
                sql,
                new AdminPaymentRowMapper()
        );

    }


}