package com.finalproject.Final.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.model.ExamBean;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ExamRepository {

    private final JdbcTemplate jdbcTemplate;


    // Get Course List by Teacher
    public List<CourseBean> getTeacherCourses(String teacherID) {

        String sql = """
                SELECT courseID, name
                FROM course
                WHERE teacherID = ?
                ORDER BY name
                """;


        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            CourseBean course = new CourseBean();

            course.setCourseId(rs.getString("courseID"));
            course.setName(rs.getString("name"));

            return course;

        }, teacherID);
    }



    // Save Exam
    public int saveExam(ExamBean bean) {


        String sql = """
                INSERT INTO exam
                (
                    examID,
                    courseID,
                    createdByID,
                    name,
                    exam_type,
                    max_score,
                    weight_percent,
                    exam_date,
                    duration_minutes,
                    status
                )
                VALUES
                (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;


        return jdbcTemplate.update(
                sql,

                UUID.randomUUID().toString(),

                bean.getCourseID(),

                bean.getCreatedByID(),

                bean.getName(),

                bean.getExamType(),

                bean.getMaxScore(),

                bean.getWeightPercent(),

                bean.getExamDate(),

                bean.getDurationMinutes(),

                bean.getStatus()
        );
    }
    public List<ExamBean> getExamListByTeacher(String teacherID) {


        String sql = """
                SELECT 
                    e.examID,
                    e.courseID,
                    e.createdByID,
                    e.name,
                    e.exam_type,
                    e.max_score,
                    e.weight_percent,
                    e.exam_date,
                    e.duration_minutes,
                    e.status,
                    c.name AS courseName

                FROM exam e

                JOIN course c
                ON e.courseID = c.courseID

                WHERE e.createdByID = ?

                ORDER BY c.name, e.exam_date DESC
                """;


        return jdbcTemplate.query(sql, (rs, rowNum) -> {


            ExamBean bean = new ExamBean();


            bean.setExamID(
                    rs.getString("examID")
            );


            bean.setCourseID(
                    rs.getString("courseID")
            );


            bean.setCreatedByID(
                    rs.getString("createdByID")
            );


            bean.setName(
                    rs.getString("name")
            );


            bean.setExamType(
                    rs.getString("exam_type")
            );


            bean.setMaxScore(
                    rs.getBigDecimal("max_score")
            );


            bean.setWeightPercent(
                    rs.getBigDecimal("weight_percent")
            );


            if(rs.getTimestamp("exam_date") != null) {

                bean.setExamDate(
                    rs.getTimestamp("exam_date")
                       .toLocalDateTime()
                );

            }


            bean.setDurationMinutes(
                    rs.getInt("duration_minutes")
            );


            bean.setStatus(
                    rs.getString("status")
            );


            // Course Name
            bean.setCourseName(
                    rs.getString("courseName")
            );


            return bean;


        }, teacherID);

    }
    public List<ExamBean> getExamListByCourse(String courseID) {

        String sql = """
                SELECT 
                    e.examID,
                    e.courseID,
                    e.createdByID,
                    e.name,
                    e.exam_type,
                    e.max_score,
                    e.weight_percent,
                    e.exam_date,
                    e.duration_minutes,
                    e.status,
                    c.name AS courseName

                FROM exam e

                JOIN course c
                ON e.courseID = c.courseID

                WHERE e.courseID = ?

                ORDER BY e.exam_date DESC
                """;


        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            ExamBean bean = new ExamBean();

            bean.setExamID(
                    rs.getString("examID")
            );

            bean.setCourseID(
                    rs.getString("courseID")
            );

            bean.setCreatedByID(
                    rs.getString("createdByID")
            );

            bean.setName(
                    rs.getString("name")
            );

            bean.setExamType(
                    rs.getString("exam_type")
            );

            bean.setMaxScore(
                    rs.getBigDecimal("max_score")
            );

            bean.setWeightPercent(
                    rs.getBigDecimal("weight_percent")
            );

            if (rs.getTimestamp("exam_date") != null) {

                bean.setExamDate(
                    rs.getTimestamp("exam_date")
                       .toLocalDateTime()
                );
            }


            bean.setDurationMinutes(
                    rs.getInt("duration_minutes")
            );


            bean.setStatus(
                    rs.getString("status")
            );


            bean.setCourseName(
                    rs.getString("courseName")
            );


            return bean;


        }, courseID);

    }
    public ExamBean getExamByID(String examID){


        String sql = """
                SELECT
                    examID,
                    courseID,
                    createdByID,
                    name,
                    exam_type,
                    max_score,
                    weight_percent,
                    exam_date,
                    duration_minutes,
                    status

                FROM exam

                WHERE examID = ?
                """;


        return jdbcTemplate.queryForObject(
                sql,
                (rs,rowNum)->{


                    ExamBean bean = new ExamBean();


                    bean.setExamID(
                        rs.getString("examID")
                    );


                    bean.setCourseID(
                        rs.getString("courseID")
                    );


                    bean.setCreatedByID(
                        rs.getString("createdByID")
                    );


                    bean.setName(
                        rs.getString("name")
                    );


                    bean.setExamType(
                        rs.getString("exam_type")
                    );


                    bean.setMaxScore(
                        rs.getBigDecimal("max_score")
                    );


                    bean.setWeightPercent(
                        rs.getBigDecimal("weight_percent")
                    );


                    if(rs.getTimestamp("exam_date") != null){

                        bean.setExamDate(
                            rs.getTimestamp("exam_date")
                            .toLocalDateTime()
                        );
                    }


                    bean.setDurationMinutes(
                        rs.getInt("duration_minutes")
                    );


                    bean.setStatus(
                        rs.getString("status")
                    );


                    return bean;

                },
                examID
        );

    }
    public int updateExam(ExamBean bean){

        String sql = """
                UPDATE exam
                SET
                    courseID=?,
                    name=?,
                    exam_type=?,
                    max_score=?,
                    weight_percent=?,
                    exam_date=?,
                    duration_minutes=?,
                    status=?,
                    updated_at=NOW()

                WHERE examID=?
                """;


        return jdbcTemplate.update(
                sql,

                bean.getCourseID(),
                bean.getName(),
                bean.getExamType(),
                bean.getMaxScore(),
                bean.getWeightPercent(),
                bean.getExamDate(),
                bean.getDurationMinutes(),
                bean.getStatus(),
                bean.getExamID()
        );
    }
    public int updateStatus(String examID, String status){

        String sql = """
            UPDATE exam
            SET status = ?
            WHERE examID = ?
        """;

        return jdbcTemplate.update(sql, status, examID);
    }
}