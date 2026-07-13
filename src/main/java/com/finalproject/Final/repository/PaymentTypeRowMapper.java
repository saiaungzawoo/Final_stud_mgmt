package com.finalproject.Final.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.finalproject.Final.model.PaymentTypeBean;

public class PaymentTypeRowMapper implements RowMapper<PaymentTypeBean> {

    @Override
    public PaymentTypeBean mapRow(ResultSet rs, int rowNum) throws SQLException {

        PaymentTypeBean bean = new PaymentTypeBean();

        bean.setPaymentTypeId(
                rs.getString("paymentTypeID"));

        bean.setName(
                rs.getString("name"));

        bean.setDescription(
                rs.getString("description"));

        return bean;
    }

}