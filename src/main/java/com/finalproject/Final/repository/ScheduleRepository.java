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
	public boolean scheduleExists(String courseID){

	    String sql = """
	            SELECT COUNT(*)
	            FROM schedule
	            WHERE courseID=?
	            """;

	    Integer count = jdbc.queryForObject(
	            sql,
	            Integer.class,
	            courseID);

	    return count != null && count > 0;

	}
	public boolean attendanceExistsByCourse(String courseID){

	    String sql = """
	            SELECT COUNT(*)
	            FROM attendance a
	            JOIN schedule s
	            ON a.scheduleID=s.scheduleID
	            WHERE s.courseID=?
	            """;

	    Integer count = jdbc.queryForObject(
	            sql,
	            Integer.class,
	            courseID);

	    return count != null && count > 0;

	}
	public int deleteScheduleByCourse(String courseID){

	    String sql = """
	            DELETE
	            FROM schedule
	            WHERE courseID=?
	            """;

	    return jdbc.update(sql, courseID);

	}
	public ScheduleBean getFirstSchedule(String courseId){

	    String sql = """
	            SELECT *
	            FROM schedule
	            WHERE courseID=?
	            ORDER BY schedule_date
	            LIMIT 1
	            """;

	    return jdbc.queryForObject(
	            sql,
	            new ScheduleRowMapper(),
	            courseId
	    );
	}
	public List<String> getRepeatDays(String courseID){

	    String sql = """
	        SELECT DISTINCT DAYNAME(schedule_date) AS dayName
	        FROM schedule
	        WHERE courseID=?
	        """;

	    return jdbc.query(
	            sql,
	            (rs,rowNum)->rs.getString("dayName").toUpperCase(),
	            courseID
	    );
	}
	public String getTopicPrefix(String courseId){

	    String sql = """
	            SELECT topic
	            FROM schedule
	            WHERE courseID=?
	            ORDER BY schedule_date
	            LIMIT 1
	            """;

	    String topic = jdbc.queryForObject(
	            sql,
	            String.class,
	            courseId
	    );

	    if(topic==null){
	        return "";
	    }

	    return topic.replaceAll("\\s+\\d+$","");
	}
}
