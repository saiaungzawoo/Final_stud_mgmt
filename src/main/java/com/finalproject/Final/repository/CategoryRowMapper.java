package com.finalproject.Final.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.finalproject.Final.model.CourseCategoryBean;

;

public class CategoryRowMapper implements RowMapper<CourseCategoryBean> {

    @Override
    public CourseCategoryBean mapRow(ResultSet rs, int rowNum) throws SQLException {

        CourseCategoryBean c = new CourseCategoryBean();

        c.setCourseCategoryId(rs.getString("courseCategoryID"));
        c.setName(rs.getString("name"));
        c.setDescription(rs.getString("description"));
        c.setIsActive(rs.getInt("is_active"));

        if(rs.getTimestamp("created_at") != null){
            c.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }

        return c;
    }
}