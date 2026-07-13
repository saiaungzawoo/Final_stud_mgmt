package com.finalproject.Final.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.finalproject.Final.model.PaymentBean;

public class PaymentRowMapper implements RowMapper<PaymentBean> {

    @Override
    public PaymentBean mapRow(ResultSet rs, int rowNum) throws SQLException {

        PaymentBean p = new PaymentBean();

        p.setPaymentId(rs.getString("paymentID"));
        p.setEnrollmentId(rs.getString("enrollmentID"));
        p.setInstallmentPlanId(rs.getString("installmentPlanID"));
        p.setPaymentMethodId(rs.getString("paymentMethodID"));
        p.setPaymentMethodName(
        	    rs.getString("paymentMethodName")
        	);
        p.setPaymentTypeName(rs.getString("paymentTypeName"));
        
        p.setStudentName(rs.getString("studentName"));
        
        try {
            p.setInstallmentNumber(
                    (Integer) rs.getObject("installment_number"));
        } catch (SQLException ignored) {}

        try {
            p.setInstallmentCount(
                    (Integer) rs.getObject("installment_count"));
        } catch (SQLException ignored) {}

        try {
            p.setAmountDue(
                    rs.getDouble("amount_due"));
        } catch (SQLException ignored) {}

        try {
            p.setPaidAmount(
                    rs.getDouble("paid_amount"));
        } catch (SQLException ignored) {}

        try {
            if (rs.getDate("due_date") != null) {
                p.setDueDate(
                        rs.getDate("due_date").toLocalDate());
            }
        } catch (SQLException ignored) {}

        p.setAmount(rs.getDouble("amount"));

        if (rs.getDate("payment_date") != null) {
            p.setPaymentDate(rs.getDate("payment_date").toLocalDate());
        }

        p.setTransactionReference(rs.getString("transaction_reference"));
        p.setStatus(rs.getString("status"));
        p.setNotes(rs.getString("notes"));
        p.setProcessedById(rs.getString("processedByID"));

        if (rs.getTimestamp("created_at") != null) {
            p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }

        if (rs.getTimestamp("updated_at") != null) {
            p.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        }

        return p;
    }
}