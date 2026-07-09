package com.finalproject.Final.repository;

import com.finalproject.Final.model.PaymentBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;



public class PaymentRowMapper implements RowMapper<PaymentBean> {

    @Override
    public PaymentBean mapRow(ResultSet rs, int rowNum) throws SQLException {

        PaymentBean p = new PaymentBean();

        p.setId(rs.getInt("id"));
        p.setAmount(rs.getInt("amount"));

        if (rs.getDate("payment_date") != null) {
            p.setPaymentDate(rs.getDate("payment_date").toLocalDate());
        }

        p.setPaymentMethod(rs.getString("payment_method"));
        p.setPaymentStatus(rs.getString("payment_status"));
        p.setCourseId(rs.getInt("course_id"));
        p.setTransactionReference(rs.getString("transaction_reference"));
        p.setReceiptDownloaded(rs.getInt("receipt_downloaded"));
        p.setCreatedAt(rs.getTimestamp("created_at") != null
                ? rs.getTimestamp("created_at").toLocalDateTime()
                : null);

        p.setUpdatedAt(rs.getTimestamp("updated_at") != null
                ? rs.getTimestamp("updated_at").toLocalDateTime()
                : null);

        return p;
    }
}