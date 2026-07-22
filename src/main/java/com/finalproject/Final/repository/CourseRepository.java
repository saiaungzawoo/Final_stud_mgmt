package com.finalproject.Final.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.CourseBean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

@Repository
public class CourseRepository {

    @Autowired
    private JdbcTemplate jdbc;
    
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final CourseRowMapper mapper = new CourseRowMapper();

    // 🔹 GET ALL COURSES
    public List<CourseBean> findAll() {

        String sql =
            "SELECT c.*, " +
            "sc.name AS subcategory_name, " +
            "cc.name AS category_name, " +
            "u.name AS teacher_name " +
            "FROM course c " +
            "JOIN subcategory sc ON c.subcategoryID = sc.subcategoryID " +
            "JOIN course_category cc ON c.courseCategoryID = cc.courseCategoryID " +
            "JOIN user u ON c.teacherID = u.userID";

        return jdbc.query(sql, mapper);
    }

    // 🔹 GET BY ID
    public CourseBean findById(String courseId) {

        String sql =
            "SELECT c.*, " +
            "sc.name AS subcategory_name, " +
            "cc.name AS category_name, " +
            "u.name AS teacher_name " +
            "FROM course c " +
            "JOIN subcategory sc ON c.subcategoryID = sc.subcategoryID " +
            "JOIN course_category cc ON c.courseCategoryID = cc.courseCategoryID " +
            "JOIN user u ON c.teacherID = u.userID " +
            "WHERE c.courseID = ?";

        return jdbc.queryForObject(sql, mapper, courseId);
    }

    // 🔹 SAVE
    public void save(CourseBean c) {

        String sql =
            "INSERT INTO course (" +
            "courseID," +
            "courseCategoryID," +
            "subcategoryID," +
            "teacherID," +
            "createdByID," +
            "name," +
            "description," +
            "duration_weeks," +
            "fee," +
            "level," +
            "status," +
            "seats_total," +
            "seats_available," +
            "thumbnail_path," +
            "allow_installment," +
            "allow_scholarship," +
            "created_at," +
            "updated_at" +
            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

        jdbc.update(sql,

            c.getCourseId(),
            c.getCourseCategoryId(),
            c.getSubcategoryId(),
            c.getTeacherId(),
            c.getCreatedBy(),

            c.getName(),
            c.getDescription(),
            c.getDurationWeeks(),

            c.getFee(),

            c.getLevel(),
            c.getStatus(),

            c.getSeatsTotal(),
            c.getSeatsTotal(),

            c.getThumbnailPath(),

            c.getAllowedInstallment(),
            c.getAllowedScholarship()
        );
    }

    // 🔹 UPDATE
    public void update(CourseBean c) {

        String sql =
            "UPDATE course SET " +
            "courseCategoryID=?," +
            "subcategoryID=?," +
            "teacherID=?," +
            "createdByID=?," +
            "name=?," +
            "description=?," +
            "duration_weeks=?," +
            "fee=?," +
            "level=?," +
            "status=?," +
            "seats_total=?," +
            "seats_available=?," +
            "thumbnail_path=?," +
            "allow_installment=?," +
            "allow_scholarship=?," +
            "updated_at=NOW() " +
            "WHERE courseID=?";

        jdbc.update(sql,

            c.getCourseCategoryId(),
            c.getSubcategoryId(),
            c.getTeacherId(),
            c.getCreatedBy(),

            c.getName(),
            c.getDescription(),
            c.getDurationWeeks(),

            c.getFee(),

            c.getLevel(),
            c.getStatus(),

            c.getSeatsTotal(),
            c.getSeatsAvailable(),

            c.getThumbnailPath(),

            c.getAllowedInstallment(),
            c.getAllowedScholarship(),

            c.getCourseId()
        );
    }
    
    public int getSeatsAvailable(String courseId) {

        String sql =
            "SELECT seats_available FROM course WHERE courseID = ?";

        return jdbc.queryForObject(sql, Integer.class, courseId);
    }
    
    public void decreaseSeat(String courseId) {

        String sql =
            "UPDATE course " +
            "SET seats_available = seats_available - 1 " +
            "WHERE courseID = ? " +
            "AND seats_available > 0";

        jdbc.update(sql, courseId);
    }

    // 🔹 DELETE
    public void delete(String courseId) {

        jdbc.update(
            "DELETE FROM course WHERE courseID = ?",
            courseId
        );
    }
    
    
    public List<CourseBean> getCoursesByIds(List<String> ids) {

        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }

        String sql =
            "SELECT * " +
            "FROM course " +
            "WHERE courseID IN (:ids)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ids", ids);

        return namedParameterJdbcTemplate.query(sql, params, mapper);
    }
    
    public List<CourseBean> findByCategory(String categoryId){

        String sql =
                "SELECT c.*, " +
                "sc.name AS subcategory_name, " +
                "cc.name AS category_name, " +
                "u.name AS teacher_name " +
                "FROM course c " +
                "JOIN subcategory sc ON c.subcategoryID=sc.subcategoryID " +
                "JOIN course_category cc ON c.courseCategoryID=cc.courseCategoryID " +
                "JOIN user u ON c.teacherID=u.userID " +
                "WHERE c.courseCategoryID=?";

        return jdbc.query(sql, mapper, categoryId);

    }
    
    
    
    //new
    public int countAllCourses(){

        String sql =
                "SELECT COUNT(*) FROM course";

        return jdbc.queryForObject(sql,Integer.class);

    }



    public int countByStatus(String status){

        String sql =
                """
                SELECT COUNT(*)
                FROM course
                WHERE status=?
                """;


        return jdbc.queryForObject(
                sql,
                Integer.class,
                status
        );

    }
}