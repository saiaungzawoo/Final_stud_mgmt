package com.finalproject.Final.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.finalproject.Final.model.EnrollmentBean;

public class EnrollmentRowMapper implements RowMapper<EnrollmentBean> {

    @Override
    public EnrollmentBean mapRow(ResultSet rs, int rowNum) throws SQLException {

        EnrollmentBean e = new EnrollmentBean();

        e.setId(rs.getInt("id"));
        e.setUserId(rs.getInt("user_id"));
        e.setCourseId(rs.getInt("course_id"));

        if (rs.getDate("enrollment_date") != null) {
            e.setEnrollmentDate(rs.getDate("enrollment_date").toLocalDate());
        }

        e.setStatus(rs.getInt("status"));
        
     // optional JOIN field
        try {
            e.setCourseTitle(rs.getString("course_title"));
        } catch (Exception ignored) {}


        return e;
    }
}
