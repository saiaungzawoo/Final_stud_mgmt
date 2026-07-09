package com.finalproject.Final.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.finalproject.Final.model.CourseBean;


//fixed char 36, v7
public class CourseRowMapper implements RowMapper<CourseBean> {

	@Override
	public CourseBean mapRow(ResultSet rs, int rowNum) throws SQLException {

	    CourseBean c = new CourseBean();

	    c.setCourseId(rs.getString("courseID"));
	    c.setCourseCategoryId(rs.getString("courseCategoryID"));
	    c.setSubcategoryId(rs.getString("subcategoryID"));

	    c.setTeacherId(rs.getString("teacherID"));

	    c.setName(rs.getString("name"));
	    c.setDescription(rs.getString("description"));

	    c.setDurationWeeks(rs.getInt("duration_weeks"));

	    c.setFee(rs.getDouble("fee"));

	    c.setLevel(rs.getString("level"));
	    c.setStatus(rs.getString("status"));

	    c.setSeatsTotal(rs.getInt("seats_total"));
	    c.setSeatsAvailable(rs.getInt("seats_available"));

	    c.setThumbnailPath(rs.getString("thumbnail_path"));

	    c.setAllowedInstallment(rs.getInt("allow_installment"));
	    c.setAllowedScholarship(rs.getInt("allow_scholarship"));

	    if (hasColumn(rs, "subcategory_name")) {
	        c.setSubcategoryName(rs.getString("subcategory_name"));
	    }

	    if (hasColumn(rs, "category_name")) {
	        c.setCategoryName(rs.getString("category_name"));
	    }

	    if (hasColumn(rs, "teacher_name")) {
	        c.setTeacherName(rs.getString("teacher_name"));
	    }

	    if (rs.getTimestamp("created_at") != null) {
	        c.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
	    }

	    if (rs.getTimestamp("updated_at") != null) {
	        c.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
	    }

	    return c;
	}

	private boolean hasColumn(ResultSet rs, String column) {
	    try {
	        rs.findColumn(column);
	        return true;
	    } catch (SQLException e) {
	        return false;
	    }
	}
}