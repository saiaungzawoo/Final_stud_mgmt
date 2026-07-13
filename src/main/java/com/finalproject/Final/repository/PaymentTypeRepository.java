package com.finalproject.Final.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.PaymentTypeBean;

@Repository
public class PaymentTypeRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public List<PaymentTypeBean> getAll() {

        String sql = " SELECT *\r\n"
        		+ "                FROM payment_type\r\n"
        		+ "                ORDER BY name";
               
                

        return jdbc.query(sql, new PaymentTypeRowMapper());
    }
    
    public PaymentTypeBean findById(String paymentTypeId) {

        String sql = """
                SELECT *
                FROM payment_type
                WHERE paymentTypeID = ?
                """;

        List<PaymentTypeBean> list =
                jdbc.query(
                        sql,
                        new PaymentTypeRowMapper(),
                        paymentTypeId
                );

        return list.isEmpty()
                ? null
                : list.get(0);
    }

}