package com.finalproject.Final.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.finalproject.Final.model.PaymentMethodBean;

public class PaymentMethodRowMapper implements RowMapper<PaymentMethodBean> {

    @Override
    public PaymentMethodBean mapRow(ResultSet rs, int rowNum) throws SQLException {

        PaymentMethodBean bean = new PaymentMethodBean();

        bean.setPaymentMethodId(
                rs.getString("paymentMethodID"));

        bean.setName(
                rs.getString("name"));

        bean.setDescription(
                rs.getString("description"));

        bean.setIsActive(
                rs.getInt("is_active"));

        return bean;
    }

}