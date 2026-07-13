package com.finalproject.Final.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import com.finalproject.Final.model.SubCategoryBean;

@Repository
public class SubCategoryRepository {
	 @Autowired
	    private JdbcTemplate jdbc;

	 public List<SubCategoryBean> getAllSubCategory() {

		    String sql = "SELECT sc.id, sc.name, sc.course_category_id FROM subcategory sc";

		    return jdbc.query(sql, (rs, rowNum) -> {

		        SubCategoryBean obj = new SubCategoryBean();

				/*
				 * obj.setId(rs.getInt("id")); obj.setName(rs.getString("name"));
				 * obj.setCourseCategoryId(rs.getInt("course_category_id"));
				 */
		        return obj;
		    });
		}
	
	
}
