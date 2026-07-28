package com.finalproject.Final.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.ExamBean;
import com.finalproject.Final.model.ExamResultBean;

@Repository
public class ExamResultRepository {

    private final JdbcTemplate jdbcTemplate;

    public ExamResultRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void saveResult(ExamResultBean bean,String teacherID) {

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
    public void saveResults(List<ExamResultBean> list,String teacherID) {


        for(ExamResultBean bean : list) {


            // Skip empty score
            if(bean.getScore() == null) {
                continue;
            }



            if(resultExists(
                    bean.getExamID(),
                    bean.getUserID())) {


                updateResult(bean,teacherID);


            } else {


                saveResult(bean,teacherID);


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

                AND NOT EXISTS (
                    SELECT 1
                    FROM exam_result er
                    WHERE er.examID = e.examID
                    AND er.userID = u.userID
                )

                ORDER BY u.name
                """;


        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            ExamResultBean bean = new ExamResultBean();

            bean.setExamID(examID);

            bean.setUserID(
                rs.getString("userID")
            );

            bean.setStudentName(
                rs.getString("name")
            );


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
    public ExamResultBean getExamResultById(String gradedByID) {
        String sql = "SELECT er.exam_result_id, er.exam_id, er.user_id, u.name AS student_name, " +
                     "er.score, er.remarks, er.graded_by_id, er.graded_at " +
                     "FROM exam_result er " +
                     "LEFT JOIN users u ON er.user_id = u.user_id " +
                     "WHERE er.exam_result_id = ?";

        try {
            // queryForObject က Data မရှိရင် Exception ပစ်ပါတယ်
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                ExamResultBean bean = new ExamResultBean();
                bean.setExamResultID(rs.getString("exam_result_id"));
                bean.setExamID(rs.getString("exam_id"));
                bean.setUserID(rs.getString("user_id"));
                bean.setStudentName(rs.getString("student_name"));
                bean.setScore(rs.getBigDecimal("score"));
                bean.setRemarks(rs.getString("remarks"));
                bean.setGradedByID(rs.getString("graded_by_id"));
                if (rs.getTimestamp("graded_at") != null) {
                    bean.setGradedAt(rs.getTimestamp("graded_at").toLocalDateTime());
                }
                return bean;
            }, gradedByID);
        } catch (EmptyResultDataAccessException e) {
            // Data ရှာမတွေ့ပါက Exception မတက်ဘဲ null သာ ပြန်ပေးမည်
            return null; 
        }
    }
    
    public int updateResult(ExamResultBean examResult, String teacherId) {
        String sql = "UPDATE exam_result SET score = ?, remarks = ?, gradedByID = ?, graded_at = NOW() WHERE examResultID = ?";
        
      
        int rowsAffected = jdbcTemplate.update(
            sql,
            examResult.getScore(),
            examResult.getRemarks(),
            teacherId,
            examResult.getExamResultID() // 🟢 WHERE examResultID = ?
        );

      
        return rowsAffected;
    }
    public List<ExamBean> getCompletedExamListByCourse(String courseID) {

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
                WHERE courseID = ?
                ORDER BY exam_date DESC
                """;

        return jdbcTemplate.query(sql, new Object[]{courseID}, (rs, rowNum) -> {

            ExamBean exam = new ExamBean();

            exam.setExamID(rs.getString("examID"));
            exam.setCourseID(rs.getString("courseID"));
            exam.setCreatedByID(rs.getString("createdByID"));
            exam.setName(rs.getString("name"));
            exam.setExamType(rs.getString("exam_type"));
            exam.setMaxScore(rs.getBigDecimal("max_score"));
            exam.setWeightPercent(rs.getBigDecimal("weight_percent"));
            exam.setExamDate(rs.getTimestamp("exam_date").toLocalDateTime());
            exam.setDurationMinutes(rs.getInt("duration_minutes"));
            exam.setStatus(rs.getString("status"));

            return exam;

        })
        .stream()
        .filter(exam -> "Completed".equals(exam.getCurrentStatus()))
        .toList();
    }

}