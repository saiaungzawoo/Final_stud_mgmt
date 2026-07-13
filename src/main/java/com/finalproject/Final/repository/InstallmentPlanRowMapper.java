package com.finalproject.Final.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.finalproject.Final.model.InstallmentPlanBean;

public class InstallmentPlanRowMapper implements RowMapper<InstallmentPlanBean> {

	    @Override
	    public InstallmentPlanBean mapRow(ResultSet rs, int rowNum)
	            throws SQLException {

	        InstallmentPlanBean bean = new InstallmentPlanBean();

	        bean.setInstallmentPlanId(
	                rs.getString("installmentPlanID"));

	        bean.setEnrollmentId(
	                rs.getString("enrollmentID"));

	        bean.setInstallmentRuleItemId(
	                rs.getString("installmentRuleItemID"));

	        bean.setInstallmentNumber(
	                rs.getInt("installment_number"));

	        bean.setAmountDue(
	                rs.getDouble("amount_due"));

	        if (rs.getDate("due_date") != null) {
	            bean.setDueDate(
	                    rs.getDate("due_date").toLocalDate());
	        }

	        bean.setPaidAmount(
	                rs.getDouble("paid_amount"));

	        bean.setStatus(
	                rs.getString("status"));

	        if (rs.getTimestamp("paid_at") != null) {
	            bean.setPaidAt(
	                    rs.getTimestamp("paid_at").toLocalDateTime());
	        }

	        if (rs.getTimestamp("created_at") != null) {
	            bean.setCreatedAt(
	                    rs.getTimestamp("created_at").toLocalDateTime());
	        }

	        if (rs.getTimestamp("updated_at") != null) {
	            bean.setUpdatedAt(
	                    rs.getTimestamp("updated_at").toLocalDateTime());
	        }

	        return bean;
	    }
	}


