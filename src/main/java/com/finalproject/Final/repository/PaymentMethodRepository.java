package com.finalproject.Final.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.PaymentMethodBean;

@Repository
public class PaymentMethodRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public List<PaymentMethodBean> getAllActive() {

        String sql = "SELECT *\r\n"
        		+ "                FROM payment_method\r\n"
        		+ "                WHERE is_active = 1\r\n"
        		+ "                ORDER BY name";

        return jdbc.query(sql, new PaymentMethodRowMapper());
    }

}