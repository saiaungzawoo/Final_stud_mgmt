package com.finalproject.Final.repository;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.finalproject.Final.model.InstallmentRuleItemBean;


public class InstallmentRuleItemRowMapper 
implements RowMapper<InstallmentRuleItemBean>{


    @Override
    public InstallmentRuleItemBean mapRow(
            ResultSet rs,
            int rowNum
    ) throws SQLException {


        InstallmentRuleItemBean item =
                new InstallmentRuleItemBean();


        item.setInstallmentRuleItemId(
                rs.getString("installmentRuleItemID")
        );


        item.setInstallmentRuleId(
                rs.getString("installmentRuleID")
        );


        item.setInstallmentNumber(
                rs.getInt("installment_number")
        );


        item.setAmount(
                rs.getDouble("amount")
        );


        if(rs.getDate("due_date") != null) {

            item.setDueDate(
                rs.getDate("due_date")
                .toLocalDate()
            );

        }


        if(rs.getTimestamp("created_at") != null) {

            item.setCreatedAt(
                rs.getTimestamp("created_at")
                .toLocalDateTime()
            );

        }


        return item;

    }


}