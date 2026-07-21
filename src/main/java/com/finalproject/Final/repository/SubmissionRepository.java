package com.finalproject.Final.repository;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.AssignmentBean;
import com.finalproject.Final.model.AssignmentStatus;
import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.model.SubmissionBean;
@Repository
public class SubmissionRepository {
	 private final JdbcTemplate jdbcTemplate;

	    public SubmissionRepository(JdbcTemplate jdbcTemplate) {
	        this.jdbcTemplate = jdbcTemplate;
	    }
	public List<SubmissionBean> getSubmissionByAssignment(String assignmentID) {


	    String sql = """
	            SELECT
	                s.submissionID,
	                s.assignmentID,
	                s.userID,
	                s.file_path,
	                s.submission_text,
	                s.score,
	                s.feedback,
	                s.gradedByID,
	                s.graded_at,
	                s.submitted_at,
	                s.updated_at,

	                u.name AS studentName,

	                a.title AS assignmentTitle

	            FROM submission s


	            JOIN user u
	            ON s.userID = u.userID


	            JOIN assignment a
	            ON s.assignmentID = a.assignmentID


	            WHERE s.assignmentID = ?


	            ORDER BY s.submitted_at DESC
	            """;


	    return jdbcTemplate.query(
	            sql,
	            (rs, rowNum) -> {


	                SubmissionBean bean =
	                        new SubmissionBean();


	                bean.setSubmissionID(
	                        rs.getString("submissionID")
	                );


	                bean.setAssignmentID(
	                        rs.getString("assignmentID")
	                );


	                bean.setUserID(
	                        rs.getString("userID")
	                );


	                bean.setFilePath(
	                        rs.getString("file_path")
	                );


	                bean.setSubmissionText(
	                        rs.getString("submission_text")
	                );


	                bean.setScore(
	                        rs.getBigDecimal("score")
	                );


	                bean.setFeedback(
	                        rs.getString("feedback")
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


	                if(rs.getTimestamp("submitted_at") != null){

	                    bean.setSubmittedAt(
	                        rs.getTimestamp("submitted_at")
	                        .toLocalDateTime()
	                    );

	                }


	                if(rs.getTimestamp("updated_at") != null){

	                    bean.setUpdatedAt(
	                        rs.getTimestamp("updated_at")
	                        .toLocalDateTime()
	                    );

	                }



	                // extra display data

	                bean.setStudentName(
	                        rs.getString("studentName")
	                );


	                bean.setAssignmentTitle(
	                        rs.getString("assignmentTitle")
	                );



	                return bean;

	            },
	            assignmentID
	    );
	}
	public SubmissionBean getSubmissionDetail(String submissionID) {


	    String sql = """
	            SELECT
	                s.submissionID,
	                s.assignmentID,
	                s.userID,
	                s.file_path,
	                s.submission_text,
	                s.score,
	                s.feedback,
	                s.gradedByID,
	                s.graded_at,
	                s.submitted_at,
	                s.updated_at,

	                u.name AS studentName,

	                a.title AS assignmentTitle,
	                a.max_score

	            FROM submission s


	            JOIN user u
	            ON s.userID = u.userID


	            JOIN assignment a
	            ON s.assignmentID = a.assignmentID


	            WHERE s.submissionID = ?
	            """;


	    return jdbcTemplate.queryForObject(
	            sql,
	            (rs, rowNum) -> {


	                SubmissionBean bean =
	                        new SubmissionBean();


	                bean.setSubmissionID(
	                        rs.getString("submissionID")
	                );


	                bean.setAssignmentID(
	                        rs.getString("assignmentID")
	                );


	                bean.setUserID(
	                        rs.getString("userID")
	                );


	                bean.setStudentName(
	                        rs.getString("studentName")
	                );


	                bean.setAssignmentTitle(
	                        rs.getString("assignmentTitle")
	                );


	                bean.setFilePath(
	                        rs.getString("file_path")
	                );


	                bean.setSubmissionText(
	                        rs.getString("submission_text")
	                );


	                bean.setScore(
	                        rs.getBigDecimal("score")
	                );


	                bean.setFeedback(
	                        rs.getString("feedback")
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


	                if(rs.getTimestamp("submitted_at") != null){

	                    bean.setSubmittedAt(
	                        rs.getTimestamp("submitted_at")
	                        .toLocalDateTime()
	                    );
	                }


	                if(rs.getTimestamp("updated_at") != null){

	                    bean.setUpdatedAt(
	                        rs.getTimestamp("updated_at")
	                        .toLocalDateTime()
	                    );
	                }


	                return bean;

	            },
	            submissionID
	    );

	}
	public void updateGrade(SubmissionBean bean) {


	    String sql = """
	            UPDATE submission
	            SET
	                score = ?,
	                feedback = ?,
	                gradedByID = ?,
	                graded_at = NOW(),
	                updated_at = NOW()

	            WHERE submissionID = ?
	            """;


	    jdbcTemplate.update(
	            sql,

	            bean.getScore(),

	            bean.getFeedback(),

	            bean.getGradedByID(),

	            bean.getSubmissionID()
	    );

	}
	public List<CourseBean> getTeacherCourses(String teacherID) {


	    String sql = """
	            SELECT
	                c.courseID,
	                c.name,
	                c.description,
	                c.duration_weeks,
	                c.fee,
	                c.level,
	                c.status

	            FROM course c

	            WHERE c.teacherID = ?

	            ORDER BY c.name
	            """;


	    return jdbcTemplate.query(
	            sql,
	            (rs, rowNum) -> {


	                CourseBean bean =
	                        new CourseBean();


	                bean.setCourseId(
	                        rs.getString("courseID")
	                );

	                bean.setName(
	                        rs.getString("name")
	                );

	                bean.setDescription(
	                        rs.getString("description")
	                );

	                bean.setDurationWeeks(
	                        rs.getInt("duration_weeks")
	                );

	                bean.setFee(
	                        rs.getDouble("fee")
	                );

	                bean.setLevel(
	                        rs.getString("level")
	                );

	                bean.setStatus(
	                        rs.getString("status")
	                );

	                return bean;

	            },
	            teacherID
	    );
	}
	public List<AssignmentBean> getAssignmentByCourse(String courseID) {


	    String sql = """
	            SELECT
	                a.assignmentID,
	                a.courseID,
	                a.createdByID,
	                a.title,
	                a.description,
	                a.max_score,
	                a.weight_percent,
	                a.due_date,
	                a.status,
	                a.created_at,
	                a.updated_at,

	                c.name AS CourseName

	            FROM assignment a

	            JOIN course c
	            ON a.courseID = c.courseID

	            WHERE a.courseID = ?

	            ORDER BY a.due_date DESC
	            """;


	    return jdbcTemplate.query(
	            sql,
	            (rs, rowNum) -> {


	                AssignmentBean bean =
	                        new AssignmentBean();


	                bean.setAssignmentID(
	                        rs.getString("assignmentID")
	                );


	                bean.setCourseID(
	                        rs.getString("courseID")
	                );


	                bean.setCreatedByID(
	                        rs.getString("createdByID")
	                );


	                bean.setTitle(
	                        rs.getString("title")
	                );


	                bean.setDescription(
	                        rs.getString("description")
	                );


	                bean.setMaxScore(
	                        rs.getBigDecimal("max_score")
	                );


	                bean.setWeightPercent(
	                        rs.getBigDecimal("weight_percent")
	                );


	                if(rs.getTimestamp("due_date") != null){

	                    bean.setDueDate(
	                        rs.getTimestamp("due_date")
	                        .toLocalDateTime()
	                    );

	                }


	                bean.setStatus(
	                        AssignmentStatus.valueOf(
	                            rs.getString("status")
	                        )
	                );


	                if(rs.getTimestamp("created_at") != null){

	                    bean.setCreatedAt(
	                        rs.getTimestamp("created_at")
	                        .toLocalDateTime()
	                    );

	                }


	                if(rs.getTimestamp("updated_at") != null){

	                    bean.setUpdatedAt(
	                        rs.getTimestamp("updated_at")
	                        .toLocalDateTime()
	                    );

	                }


	                bean.setCourseName(
	                        rs.getString("CourseName")
	                );


	                return bean;

	            },
	            courseID
	    );

	}
}
