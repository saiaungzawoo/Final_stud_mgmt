package com.finalproject.Final.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.EnrollmentBean;

@Repository
public class EnrollmentRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public int save(int userId, int courseId, LocalDate date) {

        String sql = " INSERT INTO enrollment\r\n"
        		+ "            (user_id, course_id, enrollment_date, status, created_at, updated_at)\r\n"
        		+ "            VALUES (?, ?, ?, 0, NOW(), NOW())";

        jdbc.update(sql, userId, courseId, date);

        return jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
    }

    public EnrollmentBean findById(int id) {

        String sql = "SELECT * FROM enrollment WHERE id = ?";

        return jdbc.queryForObject(sql, new EnrollmentRowMapper(), id);
    }

    public List<EnrollmentBean> findByUser(int userId) {

        String sql =
            "SELECT e.*, c.name AS course_title " +
            "FROM enrollment e " +
            "JOIN course c ON e.course_id = c.id " +
            "WHERE e.user_id = ?";

        return jdbc.query(sql, new EnrollmentRowMapper(), userId);
    }

    public void updateStatus(int enrollmentId, int status) {

        jdbc.update(
            "UPDATE enrollment SET status=?, updated_at=NOW() WHERE id=?",
            status,
            enrollmentId
        );
    }
    
    
    public boolean existsByUserIdAndCourseId(int userId, int courseId) {

        String sql = " SELECT COUNT(*) \r\n"
        		+ "        FROM enrollment \r\n"
        		+ "        WHERE user_id = ? AND course_id = ?";

        Integer count = jdbc.queryForObject(sql, Integer.class, userId, courseId);
        return count != null && count > 0;
    }
}
