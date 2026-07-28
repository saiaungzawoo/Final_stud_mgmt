package com.finalproject.Final.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.FeedbackBean;

@Repository
public class FeedbackRepository {

	
	@Autowired
    JdbcTemplate jdbc;
	
	public int saveFeedback(FeedbackBean fb) {
		 System.out.println("USER ID = " + fb.getUserID());
		    System.out.println("COURSE ID = " + fb.getCourseID());
		    System.out.println("RATING = " + fb.getRating());

        String sql = """
                INSERT INTO feedback
                (feedbackID,userID,courseID,rating,comment,is_anonymous)
                VALUES(?,?,?,?,?,?)
                """;

        return jdbc.update(
                sql,
                UUID.randomUUID().toString(),
                fb.getUserID(),
                fb.getCourseID(),
                fb.getRating(),
                fb.getComment(),
                fb.getIsAnonymous()
        );
    }
	
	public FeedbackBean getCourseById(String courseID) {

	    String sql = """
	            SELECT courseID,name
	            FROM course
	            WHERE courseID=?
	            """;

	    List<FeedbackBean> list = jdbc.query(sql, (rs, rowNum) -> {

	        FeedbackBean course = new FeedbackBean();

	        course.setCourseID(rs.getString("courseID"));
	        course.setCourseName(rs.getString("name"));

	        return course;

	    }, courseID);

	    if (list.isEmpty()) {
	        return null;
	    }
return list.get(0);
	}
	
	public List<FeedbackBean> getEnrolledCourses(String userID) {

	    String sql = """
	        SELECT 
	            c.courseID,
	            c.name,

	            CASE 
	                WHEN f.feedbackID IS NULL THEN 0
	                ELSE 1
	            END AS feedbackGiven

	        FROM course c

	        JOIN enrollment e
	        ON c.courseID = e.courseID

	        LEFT JOIN feedback f
	        ON f.courseID = c.courseID
	        AND f.userID = e.userID

	        WHERE e.userID = ?
	    """;


	    return jdbc.query(sql, (rs, rowNum) -> {

	        FeedbackBean course = new FeedbackBean();

	        course.setCourseID(
	                rs.getString("courseID")
	        );

	        course.setCourseName(
	                rs.getString("name")
	        );

	        course.setFeedbackGiven(
	                rs.getInt("feedbackGiven")
	        );

return course;

	    }, userID);
	}
	
	
	// admin view 
	public List<FeedbackBean> getAllFeedback() {

	    String sql = """
	            SELECT 
	                f.feedbackID,
	                f.userID,
	                f.courseID,
	                f.rating,
	                f.comment,
	                f.is_anonymous,
	                f.created_at,
	                f.updated_at,

	                u.name AS userName,
	                c.name AS courseName

	            FROM feedback f

	            LEFT JOIN user u
	            ON f.userID = u.userID

	            LEFT JOIN course c
	            ON f.courseID = c.courseID

	            ORDER BY f.created_at DESC
	            """;


	    return jdbc.query(sql, (rs, rowNum) -> {

	        FeedbackBean feedback = new FeedbackBean();

	        feedback.setFeedbackID(
	                rs.getString("feedbackID")
	        );

	        feedback.setUserID(
	                rs.getString("userID")
	        );

	        feedback.setCourseID(
	                rs.getString("courseID")
	        );

	        feedback.setRating(
	                rs.getInt("rating")
	        );

	        feedback.setComment(
	                rs.getString("comment")
	        );

	        feedback.setIsAnonymous(
	                rs.getInt("is_anonymous")
	        );

	        feedback.setCreatedAt(
	                rs.getTimestamp("created_at")
	        );

	        feedback.setUpdatedAt(
	                rs.getTimestamp("updated_at")
	        );


	        // important for admin page
	        feedback.setUserName(
	                rs.getString("userName")
	        );

	        feedback.setCourseName(
	                rs.getString("courseName")
	        );
return feedback;

	    });
	}
	
	
	public List<FeedbackBean> getCourseFeedback(String courseID) {

	    String sql = """
	            SELECT *
	            FROM feedback
	            WHERE courseID = ?
	            ORDER BY created_at DESC
	            """;

	    return jdbc.query(sql, (rs, rowNum) -> {

	        FeedbackBean feedback = new FeedbackBean();

	        feedback.setFeedbackID(rs.getString("feedbackID"));
	        feedback.setUserID(rs.getString("userID"));
	        feedback.setCourseID(rs.getString("courseID"));
	        feedback.setRating(rs.getInt("rating"));
	        feedback.setComment(rs.getString("comment"));
	        feedback.setIsAnonymous(rs.getInt("is_anonymous"));
	        feedback.setCreatedAt(rs.getTimestamp("created_at"));
	        feedback.setUpdatedAt(rs.getTimestamp("updated_at"));

	        return feedback;

	    }, courseID);
	}
	
	
//admin for delete
    public void deleteFeedback(String feedbackID){

        String sql="DELETE FROM feedback WHERE feedbackID=?";

        jdbc.update(sql,feedbackID);
    }

}

