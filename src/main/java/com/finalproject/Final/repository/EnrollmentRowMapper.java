package com.finalproject.Final.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.finalproject.Final.model.EnrollmentBean;

public class EnrollmentRowMapper implements RowMapper<EnrollmentBean> {

    @Override
    public EnrollmentBean mapRow(ResultSet rs, int rowNum) throws SQLException {

        EnrollmentBean e = new EnrollmentBean();

        e.setEnrollmentId(rs.getString("enrollmentID"));
        e.setUserId(rs.getString("userID"));
        e.setCourseId(rs.getString("courseID"));

        e.setPaymentTypeId(rs.getString("paymentTypeID"));
        e.setInstallmentRuleId(rs.getString("installmentRuleID"));
        e.setScholarshipApplicationId(rs.getString("scholarshipApplicationID"));

        if (rs.getDate("enrollment_date") != null) {
            e.setEnrollmentDate(rs.getDate("enrollment_date").toLocalDate());
        }

        e.setOriginalFee(rs.getDouble("original_fee"));
        e.setDiscountedAmount(rs.getDouble("discount_amount"));
        e.setFinalFee(rs.getDouble("final_fee"));

        e.setPaymentStatus(rs.getString("payment_status"));
        e.setStatus(rs.getString("status"));

        if (rs.getTimestamp("created_at") != null) {
            e.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }

        if (rs.getTimestamp("updated_at") != null) {
            e.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        }

        // Optional fields from JOINs
        try {
            e.setCourseTitle(rs.getString("course_title"));
        } catch (SQLException ignored) {}

        try {
            e.setUsername(rs.getString("user_name"));
        } catch (SQLException ignored) {}
        
        try {
            e.setTeacherName(rs.getString("teacher_name"));
        } catch (SQLException ignored) {}

        try {
            e.setRemainingBalance(
                    (Double) rs.getDouble("remaining_balance"));
        } catch (SQLException ignored) {}

        try {
            e.setTotalPaid(
                    (Double) rs.getDouble("total_paid"));
        } catch (SQLException ignored) {}

        try {
            e.setTotalInstallments(
                    (Integer) rs.getInt("total_installments"));
        } catch (SQLException ignored) {}

        try {
            e.setCompletedInstallments(
                    (Integer) rs.getInt("completed_installments"));
        } catch (SQLException ignored) {}

        return e;
    }
}