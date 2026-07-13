package com.finalproject.Final.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.ScheduleBean;

@Repository
public class ScheduleRepository {
	
	@Autowired
	private JdbcTemplate jdbc;
	
	public List<ScheduleBean> findByCourseId(String courseId) {

	    String sql =
	        "SELECT * FROM schedule WHERE courseID = ?";

	    return jdbc.query(sql, new ScheduleRowMapper(), courseId);
	}
	public int getDurationWeeks(String courseId){

	    String sql = """
	            SELECT duration_weeks
	            FROM course
	            WHERE courseID=?
	            """;

	    return jdbc.queryForObject(sql, Integer.class, courseId);

	}
	public void insertSchedule(ScheduleBean obj){

	    String sql = """
	            INSERT INTO schedule
	            (
	            scheduleID,
	            courseID,
	            schedule_date,
	            start_time,
	            end_time,
	            room,
	            topic,
	            status
	            )
	            VALUES
	            (?,?,?,?,?,?,?,?)
	            """;

	    jdbc.update(sql,

	            obj.getScheduleId(),
	            obj.getCourseId(),
	            obj.getScheduleDate(),
	            obj.getStartTime(),
	            obj.getEndTime(),
	            obj.getRoom(),
	            obj.getTopic(),
	            obj.getStatus());

	}
	public List<ScheduleBean> findAllSchedule(){

	    String sql = """
	            SELECT *
	            FROM schedule
	            ORDER BY schedule_date ASC
	            """;

	    return jdbc.query(sql, new ScheduleRowMapper());

	}
	public List<ScheduleBean> findScheduleByCourse(String courseId){

	    String sql = """
	            SELECT *
	            FROM schedule
	            WHERE courseID=?
	            ORDER BY schedule_date
	            """;


	    return jdbc.query(
	            sql,
	            new ScheduleRowMapper(),
	            courseId
	    );

	}

}
