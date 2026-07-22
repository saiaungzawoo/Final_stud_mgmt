package com.finalproject.Final.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.CourseCategoryBean;


@Repository
public class CourseCategoryRepository {

    @Autowired
    private JdbcTemplate jdbc;

    private final CategoryRowMapper mapper = new CategoryRowMapper();

    public List<CourseCategoryBean> findAll(){

        String sql = """
                SELECT *
                FROM course_category
                WHERE is_active = 1
                ORDER BY name
                """;

        return jdbc.query(sql, mapper);
    }
    
    public int countCoursesByCategory(String categoryId) {

        String sql = """
            SELECT COUNT(*)
            FROM course
            WHERE courseCategoryID = ?
            """;

        return jdbc.queryForObject(sql, Integer.class, categoryId);
    }

}