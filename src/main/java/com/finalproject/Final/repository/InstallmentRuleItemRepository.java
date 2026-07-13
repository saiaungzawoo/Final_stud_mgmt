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


}