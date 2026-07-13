package com.finalproject.Final.repository;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.InstallmentRuleBean;

@Repository
public class InstallmentRuleRepository {


    @Autowired
    private JdbcTemplate jdbc;



    // Find installment rule by course

    public InstallmentRuleBean findByCourseId(
            String courseId
    ){

        String sql =
        """
        SELECT *
        FROM installment_rule
        WHERE courseID = ?
        AND is_active = 1
        LIMIT 1
        """;


        List<InstallmentRuleBean> list =
                jdbc.query(
                    sql,
                    new InstallmentRuleRowMapper(),
                    courseId
                );


        return list.isEmpty()
                ? null
                : list.get(0);

    }

}