package com.finalproject.Final.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.model.EnrollmentBean;

@Repository
public class EnrollmentRepository {

    @Autowired
    private JdbcTemplate jdbc;


    // CREATE ENROLLMENT
    public String save(String userId, String courseId, LocalDate date) {

        String enrollmentId = UUID.randomUUID().toString();

        String sql =
            "INSERT INTO enrollment " +
            "(enrollmentID, userID, courseID, paymentTypeID, installmentRuleID, scholarshipApplicationID, enrollment_date, status, created_at, updated_at) " +
            "VALUES (?, ?, ?, ?, 'Pending', NOW(), NOW())";


        jdbc.update(
            sql,
            enrollmentId,
            userId,
            courseId,
            date
        );

        return enrollmentId;
    }


    // FIND BY ID
    public EnrollmentBean findById(String enrollmentId) {

        String sql =
            "SELECT * FROM enrollment WHERE enrollmentID = ?";

        return jdbc.queryForObject(
            sql,
            new EnrollmentRowMapper(),
            enrollmentId
        );
    }


    // FIND ENROLLMENTS BY USER
    public List<EnrollmentBean> findByUser(String userId) {

        String sql =
            "SELECT e.*, " +
            "c.name AS course_title " +
            "FROM enrollment e " +
            "JOIN course c ON e.courseID = c.courseID " +
            "WHERE e.userID = ?";


        return jdbc.query(
            sql,
            new EnrollmentRowMapper(),
            userId
        );
    }


    // UPDATE STATUS
    public void updateStatus(String enrollmentId, String status) {

        String sql =
            "UPDATE enrollment " +
            "SET status=?, updated_at=NOW() " +
            "WHERE enrollmentID=?";


        jdbc.update(
            sql,
            status,
            enrollmentId
        );
    }


    // CHECK DUPLICATE ENROLLMENT
    public boolean existsByUserIdAndCourseId(String userId, String courseId) {

        String sql =
            "SELECT COUNT(*) " +
            "FROM enrollment " +
            "WHERE userID=? AND courseID=?";


        Integer count =
            jdbc.queryForObject(
                sql,
                Integer.class,
                userId,
                courseId
            );


        return count != null && count > 0;
    }


    // GET COURSES USER ENROLLED IN
    public List<CourseBean> getEnrolledCourses(String userId) {


        String sql =
            "SELECT " +
            "c.*, " +
            "cat.name AS category_name, " +
            "sub.name AS subcategory_name, " +
            "u.name AS teacher_name " +

            "FROM enrollment e " +

            "JOIN course c " +
            "ON e.courseID = c.courseID " +

            "LEFT JOIN course_category cat " +
            "ON c.courseCategoryID = cat.courseCategoryID " +

            "LEFT JOIN subcategory sub " +
            "ON c.subcategoryID = sub.subcategoryID " +

            "LEFT JOIN `user` u " +
            "ON c.teacherID = u.userID " +

            "WHERE e.userID = ? " +
            "AND e.status = 'Active'";


        return jdbc.query(
            sql,
            new CourseRowMapper(),
            userId
        );
    }

}