package com.finalproject.Final.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.finalproject.Final.model.PaymentHistoryBean;


public class PaymentHistoryRowMapper 
        implements RowMapper<PaymentHistoryBean> {


    @Override
    public PaymentHistoryBean mapRow(
            ResultSet rs,
            int rowNum
    ) throws SQLException {


        PaymentHistoryBean history =
                new PaymentHistoryBean();


        history.setPaymentId(
                rs.getString("paymentID")
        );


        history.setAmount(
                rs.getDouble("amount")
        );


        history.setPaymentMethodName(
                rs.getString("paymentMethodName")
        );


        if(rs.getDate("payment_date") != null){

            history.setPaymentDate(
                    rs.getDate("payment_date")
                    .toLocalDate()
            );

        }


        history.setStatus(
                rs.getString("status")
        );


        history.setTransactionReference(
                rs.getString("transaction_reference")
        );


        return history;

    }

}