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

		    String sql = "SELECT sc.subcategoryID, sc.name, sc.courseCategoryID FROM subcategory sc";

		    return jdbc.query(sql, (rs, rowNum) -> {

		        SubCategoryBean obj = new SubCategoryBean();

		        obj.setSubCategoryId(rs.getString("subcategoryID"));
		        obj.setName(rs.getString("name"));
		        obj.setCourseCategoryId(rs.getString("courseCategoryID"));

		        return obj;
		    });
		}
	
	
}
