package com.finalproject.Final.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.finalproject.Final.model.ScheduleBean;

public class ScheduleRowMapper implements RowMapper<ScheduleBean> {

    @Override
    public ScheduleBean mapRow(ResultSet rs, int rowNum) throws SQLException {

        ScheduleBean schedule = new ScheduleBean();

        schedule.setScheduleId(rs.getString("scheduleID"));
        schedule.setCourseId(rs.getString("courseID"));

        if (rs.getDate("schedule_date") != null) {
            schedule.setScheduleDate(rs.getDate("schedule_date").toLocalDate());
        }

        if (rs.getTime("start_time") != null) {
            schedule.setStartTime(rs.getTime("start_time").toLocalTime());
        }

        if (rs.getTime("end_time") != null) {
            schedule.setEndTime(rs.getTime("end_time").toLocalTime());
        }

        schedule.setRoom(rs.getString("room"));
        schedule.setTopic(rs.getString("topic"));
        schedule.setStatus(rs.getString("status"));

        if (rs.getTimestamp("created_at") != null) {
            schedule.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }

        return schedule;
    }
}