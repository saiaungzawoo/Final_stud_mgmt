package com.finalproject.Final.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.finalproject.Final.model.InstallmentRuleBean;


public class InstallmentRuleRowMapper implements RowMapper<InstallmentRuleBean> {


    @Override
    public InstallmentRuleBean mapRow(ResultSet rs, int rowNum) throws SQLException {


        InstallmentRuleBean rule = new InstallmentRuleBean();


        rule.setInstallmentRuleId(
                rs.getString("installmentRuleID")
        );


        rule.setCourseId(
                rs.getString("courseID")
        );


        rule.setName(
                rs.getString("name")
        );


        rule.setInstallmentCount(
                rs.getInt("installment_count")
        );


        rule.setIsActive(
                rs.getInt("is_active")
        );


        rule.setCreatedById(
                rs.getString("createdByID")
        );


        if(rs.getTimestamp("created_at") != null) {

            rule.setCreatedAt(
                rs.getTimestamp("created_at")
                .toLocalDateTime()
            );

        }


        if(rs.getTimestamp("updated_at") != null) {

            rule.setUpdatedAt(
                rs.getTimestamp("updated_at")
                .toLocalDateTime()
            );

        }


        return rule;
    }

}