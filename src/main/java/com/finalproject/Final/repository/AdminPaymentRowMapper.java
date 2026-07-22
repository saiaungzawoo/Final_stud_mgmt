package com.finalproject.Final.repository;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.finalproject.Final.dto.AdminPaymentDTO;


public class AdminPaymentRowMapper implements RowMapper<AdminPaymentDTO> {


    @Override
    public AdminPaymentDTO mapRow(ResultSet rs, int rowNum) throws SQLException {


        AdminPaymentDTO payment = new AdminPaymentDTO(

                rs.getString("studentName"),

                rs.getString("courseName"),

                rs.getString("paymentType"),

                rs.getDouble("amount"),

                rs.getString("status"),

                rs.getDate("payment_date") != null
                        ? rs.getDate("payment_date").toLocalDate()
                        : null
        );


        return payment;
    }

}