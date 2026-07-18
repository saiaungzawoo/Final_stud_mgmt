package com.finalproject.Final.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.finalproject.Final.model.SubCategoryBean;



public class SubCategoryRowMapper implements RowMapper<SubCategoryBean> {

    @Override
    public SubCategoryBean mapRow(ResultSet rs, int rowNum) throws SQLException {

        SubCategoryBean sub = new SubCategoryBean();

        sub.setSubCategoryId(rs.getString("subcategoryID"));
        sub.setCourseCategoryId(rs.getString("courseCategoryID"));

        sub.setName(rs.getString("name"));
        sub.setDescription(rs.getString("description"));

        sub.setIsActive(rs.getInt("is_active"));

        if (rs.getTimestamp("created_at") != null) {
            sub.setCreatedAt(
                    rs.getTimestamp("created_at").toLocalDateTime()
            );
        }

        return sub;
    }
}