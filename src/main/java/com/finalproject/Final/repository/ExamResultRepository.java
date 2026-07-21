package com.finalproject.Final.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.ExamResultBean;

@Repository
public class ExamResultRepository {

    private final JdbcTemplate jdbcTemplate;

    public ExamResultRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void saveResult(ExamResultBean bean) {

        String sql = """
                INSERT INTO exam_result
                (
                    examResultID,
                    examID,
                    userID,
                    score,
                    remarks,
                    gradedByID,
                    graded_at
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;


        String teacherID = "00ee5b4b-7a6f-11f1-8f4f-183d2d227d02";


        jdbcTemplate.update(sql,
                UUID.randomUUID().toString(),
                bean.getExamID(),
                bean.getUserID(),
                bean.getScore(),
                bean.getRemarks(),
                teacherID,
                LocalDateTime.now()
        );
    }



    // ဒီနေရာမှာ ထည့်မယ်
    public void saveResults(List<ExamResultBean> list) {


        for(ExamResultBean bean : list) {


            // Skip empty score
            if(bean.getScore() == null) {
                continue;
            }



            if(resultExists(
                    bean.getExamID(),
                    bean.getUserID())) {


                updateResult(bean);


            } else {


                saveResult(bean);


            }

        }

    }

    public List<ExamResultBean> getStudentListByExam(String examID) {

        String sql = """
                SELECT 
                    u.userID,
                    u.name
                FROM exam e
                JOIN enrollment en
                    ON e.courseID = en.courseID
                JOIN user u
                    ON en.userID = u.userID
                WHERE e.examID = ?
                AND en.status = 'Active'
                ORDER BY u.name
                """;


        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            ExamResultBean bean = new ExamResultBean();

            bean.setExamID(examID);
            bean.setUserID(rs.getString("userID"));
            bean.setStudentName(rs.getString("name"));

            return bean;

        }, examID);
    }
    public boolean resultExists(String examID, String userID) {

        String sql = """
                SELECT COUNT(*)
                FROM exam_result
                WHERE examID = ?
                AND userID = ?
                """;


        Integer count = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                examID,
                userID
        );


        return count != null && count > 0;
    }
    public void updateResult(ExamResultBean bean) {

        String sql = """
                UPDATE exam_result
                SET
                    score = ?,
                    remarks = ?,
                    gradedByID = ?,
                    graded_at = ?
                WHERE examID = ?
                AND userID = ?
                """;


        String teacherID =
            "00ee5b4b-7a6f-11f1-8f4f-183d2d227d02";


        jdbcTemplate.update(sql,

                bean.getScore(),
                bean.getRemarks(),
                teacherID,
                LocalDateTime.now(),

                bean.getExamID(),
                bean.getUserID()
        );
    }
    public List<ExamResultBean> getResultListByExam(String examID) {

        String sql = """
                SELECT
                    er.examResultID,
                    er.examID,
                    er.userID,
                    u.name AS studentName,
                    er.score,
                    er.remarks,
                    er.gradedByID,
                    er.graded_at,
                    er.created_at

                FROM exam_result er

                JOIN user u
                ON er.userID = u.userID

                WHERE er.examID = ?

                ORDER BY u.name
                """;


        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            ExamResultBean bean = new ExamResultBean();


            bean.setExamResultID(
                    rs.getString("examResultID")
            );


            bean.setExamID(
                    rs.getString("examID")
            );


            bean.setUserID(
                    rs.getString("userID")
            );


            bean.setStudentName(
                    rs.getString("studentName")
            );


            bean.setScore(
                    rs.getBigDecimal("score")
            );


            bean.setRemarks(
                    rs.getString("remarks")
            );


            bean.setGradedByID(
                    rs.getString("gradedByID")
            );


            if(rs.getTimestamp("graded_at") != null) {

                bean.setGradedAt(
                    rs.getTimestamp("graded_at")
                    .toLocalDateTime()
                );

            }


            if(rs.getTimestamp("created_at") != null) {

                bean.setCreatedAt(
                    rs.getTimestamp("created_at")
                    .toLocalDateTime()
                );

            }


            return bean;


        }, examID);
    }
    public ExamResultBean getResultByID(String examResultID) {


        String sql = """
                SELECT
                    er.examResultID,
                    er.examID,
                    er.userID,
                    u.name AS studentName,
                    er.score,
                    er.remarks

                FROM exam_result er

                JOIN user u
                ON er.userID = u.userID

                WHERE er.examResultID = ?
                """;


        return jdbcTemplate.queryForObject(
                sql,
                (rs, rowNum) -> {


                    ExamResultBean bean =
                            new ExamResultBean();


                    bean.setExamResultID(
                            rs.getString("examResultID")
                    );


                    bean.setExamID(
                            rs.getString("examID")
                    );


                    bean.setUserID(
                            rs.getString("userID")
                    );


                    bean.setStudentName(
                            rs.getString("studentName")
                    );


                    bean.setScore(
                            rs.getBigDecimal("score")
                    );


                    bean.setRemarks(
                            rs.getString("remarks")
                    );


                    return bean;

                },
                examResultID       
        		);
    }
    public List<ExamResultBean> getResultByStudent(String userID) {


        String sql = """
                SELECT
                    er.examResultID,
                    er.examID,
                    er.userID,
                    er.score,
                    er.remarks,
                    er.gradedByID,
                    er.graded_at,

                    e.name AS examName,
                    e.max_score,
                    e.exam_type,

                    c.name AS courseName

                FROM exam_result er


                JOIN exam e
                ON er.examID = e.examID


                JOIN course c
                ON e.courseID = c.courseID


                WHERE er.userID = ?


                ORDER BY e.exam_date DESC
                """;


        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {


                    ExamResultBean bean =
                            new ExamResultBean();


                    bean.setExamResultID(
                            rs.getString("examResultID")
                    );


                    bean.setExamID(
                            rs.getString("examID")
                    );


                    bean.setUserID(
                            rs.getString("userID")
                    );


                    bean.setScore(
                            rs.getBigDecimal("score")
                    );


                    bean.setRemarks(
                            rs.getString("remarks")
                    );


                    bean.setGradedByID(
                            rs.getString("gradedByID")
                    );


                    if(rs.getTimestamp("graded_at") != null){

                        bean.setGradedAt(
                            rs.getTimestamp("graded_at")
                            .toLocalDateTime()
                        );

                    }


                    // extra display data
                    bean.setExamName(
                            rs.getString("examName")
                    );


                    bean.setCourseName(
                            rs.getString("courseName")
                    );


                    bean.setMaxScore(
                            rs.getBigDecimal("max_score")
                    );


                    bean.setExamType(
                            rs.getString("exam_type")
                    );


                    return bean;

                },
                userID
        );
    }

}