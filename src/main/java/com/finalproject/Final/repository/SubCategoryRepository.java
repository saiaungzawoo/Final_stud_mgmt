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

	 public List<SubCategoryBean> findAll() {

		    String sql = """
		        SELECT *
		        FROM subcategory
		        WHERE is_active = 1
		        ORDER BY name
		        """;

		    return jdbc.query(sql, new SubCategoryRowMapper());
		}
	
	
}
