package com.finalproject.Final.repository;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.InstallmentRuleItemBean;


@Repository
public class InstallmentRuleItemRepository {


    @Autowired
    private JdbcTemplate jdbc;



    // Find all installment steps

    public List<InstallmentRuleItemBean> findByRuleId(
            String installmentRuleId
    ){


        String sql =
        """
        SELECT *
        FROM installment_rule_item
        WHERE installmentRuleID = ?
        ORDER BY installment_number
        """;



        return jdbc.query(
                sql,
                new InstallmentRuleItemRowMapper(),
                installmentRuleId
        );


    }
    
    public void save(
            InstallmentRuleItemBean item
    ) {


        String sql =
        """
        INSERT INTO installment_rule_item
        (
            installmentRuleItemID,
            installmentRuleID,
            installment_number,
            amount,
            due_date,
            created_at
        )
        VALUES
        (?, ?, ?, ?, ?, NOW())
        """;


        jdbc.update(
            sql,

            item.getInstallmentRuleItemId(),
            item.getInstallmentRuleId(),
            item.getInstallmentNumber(),
            item.getAmount(),
            item.getDueDate()

        );


    }


}