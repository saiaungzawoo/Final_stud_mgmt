package com.finalproject.Final.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.dto.CourseCategoryDTO;
import com.finalproject.Final.model.CourseCategoryBean;



/**
 * 
 * Course Category CRUD
 * 
 * @author WYTM
 *
 */

//fixed sazo

@Repository
public class CourseCategoryRepository {

	@Autowired
	JdbcTemplate jdbc;
	
	public int createdCourseCategory(CourseCategoryDTO courseCategory) {
		
		String sql = "insert into course_category (name) values (?)";
		
		return jdbc.update(sql,courseCategory.getName());
		
	}
	
	
	/*
	 * public List<CourseCategoryBean> getAllCourseCategory(){
	 * 
	 * 
	 * 
	 * String sql = "select * from course_category";
	 * 
	 * return jdbc.query(sql, (rs,rowCont) -> new CourseCategoryBean(
	 * rs.getInt("id"), rs.getString("name") ) );
	 * 
	 * }
	 */
	
	public CourseCategoryBean getCourseCategoryById(String courseCategoryId) {

	    String sql = "SELECT * FROM course_category WHERE courseCategoryID = ?";

	    return jdbc.queryForObject(sql,
	            (rs, rowNum) -> new CourseCategoryBean(
	                    rs.getString("courseCategoryID"),
	                    rs.getString("name"),
	                    rs.getString("description"),
	                    rs.getInt("is_active"),
	                    rs.getTimestamp("created_at") != null
	                            ? rs.getTimestamp("created_at").toLocalDateTime()
	                            : null
	            ),
	            courseCategoryId);
	}
	
	public int updateCourseCategory(String CourseCatId, String courseCatName) {
		
		String sql = "update course_category set name=? where id=?";
		
		return  jdbc.update(sql,courseCatName,CourseCatId);
		
	}
	
	public int deleteCourseCategory(String courseCategoryId) {
		
		String sql = "delete from course_category where id=?";
		
		return jdbc.update(sql,courseCategoryId);
	}
}
