package com.finalproject.Final.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.SubmissionBean;

@Repository
public class StudentSubmissionRepository {
@Autowired
private JdbcTemplate jdbc;

// 1 student view 
public int submitAssignment(SubmissionBean bean) {

    String sql = """
INSERT INTO submission
(submissionID,assignmentID,userID,file_path,submission_text,submitted_at)
 VALUES(?,?,?,?,?,NOW())
        """;

    return jdbc.update(sql,

            UUID.randomUUID().toString(),
            bean.getAssignmentID(),
            bean.getUserID(),
            bean.getFilePath(),
            bean.getSubmissionText()

    );

}

// 2 student view own submission
public List<SubmissionBean> getStudentSubmission(String userID){

    String sql = """
            SELECT s.*,a.title AS assignmentTitle
            FROM submission s
            JOIN assignment a
            ON s.assignmentID=a.assignmentID
            WHERE s.userID=?
            ORDER BY s.submitted_at DESC
            """;

    return jdbc.query(sql,(rs,row)->{

        SubmissionBean bean=new SubmissionBean();
bean.setSubmissionID(rs.getString("submissionID"));
bean.setAssignmentID(rs.getString("assignmentID"));
bean.setUserID(rs.getString("userID"));
bean.setFilePath(rs.getString("file_path"));
bean.setSubmissionText(rs.getString("submission_text"));
bean.setScore(rs.getBigDecimal("score"));
bean.setFeedback(rs.getString("feedback"));
bean.setAssignmentTitle(rs.getString("assignmentTitle"));
return bean;
},userID);

}


// 3 student view 
public SubmissionBean getSubmissionByID(String id){

    String sql="""
SELECT *
FROM submission
WHERE submissionID=?
""";

    return jdbc.queryForObject(sql,(rs,row)->{

        SubmissionBean bean=new SubmissionBean();
bean.setSubmissionID(rs.getString("submissionID"));
bean.setAssignmentID(rs.getString("assignmentID"));
bean.setUserID(rs.getString("userID"));
bean.setFilePath(rs.getString("file_path"));
bean.setSubmissionText(rs.getString("submission_text"));
bean.setScore(rs.getBigDecimal("score"));
bean.setFeedback(rs.getString("feedback"));
return bean;
},id);

}



//4 teacher view all submission
//public List<SubmissionBean> getAllSubmission(){
//
//    String sql="""
//SELECT s.*,
//u.name studentName,a.title assignmentTitle FROM submission s
//JOIN user u
//ON s.userID=u.userID
//JOIN assignment a
//ON s.assignmentID=a.assignmentID
//ORDER BY s.submitted_at DESC
//""";
//
//    return jdbc.query(sql,(rs,row)->{
//
//        SubmissionBean bean=new SubmissionBean();
//
//        bean.setSubmissionID(rs.getString("submissionID"));
//        bean.setAssignmentID(rs.getString("assignmentID"));
//        bean.setUserID(rs.getString("userID"));
//        bean.setStudentName(rs.getString("studentName"));
//        bean.setAssignmentTitle(rs.getString("assignmentTitle"));
//        bean.setFilePath(rs.getString("file_path"));
//        bean.setSubmissionText(rs.getString("submission_text"));
//        bean.setScore(rs.getBigDecimal("score"));
//        bean.setFeedback(rs.getString("feedback"));
// return bean;
//
//    });
//
//}


//// 5 grade teacher view give score and feedback
//public int gradeSubmission(SubmissionBean bean){
//
//    String sql="""
//UPDATE submission
// SET
//score=?,feedback=?,gradedByID=?,graded_at=NOW(),updated_at=NOW() WHERE submissionID=?
//""";
//
//    return jdbc.update(sql,
//
//            bean.getScore(),
//            bean.getFeedback(),
//            bean.getGradedByID(),
//            bean.getSubmissionID()
//
//            );
//}

// Get All Published Assignments
public List<SubmissionBean> getAllAssignment() {

    String sql = """
            SELECT
                a.assignmentID,
                a.courseID,
                c.name,
                a.createdByID,
                a.title,
                a.description,
                a.max_score,
                a.weight_percent,
                a.due_date,
                a.status,
                a.created_at,
                a.updated_at
            FROM assignment a
            JOIN course c
                ON a.courseID = c.courseID
            WHERE a.status = 'Published'
            ORDER BY a.due_date ASC
            """;

    return jdbc.query(sql, (rs, rowNum) -> {

      SubmissionBean bean = new SubmissionBean();

        bean.setAssignmentID(rs.getString("assignmentID"));
        bean.setCourseID(rs.getString("courseID"));
        bean.setCourseName(rs.getString("name"));
        bean.setCreatedByID(rs.getString("createdByID"));

        bean.setTitle(rs.getString("title"));
        bean.setDescription(rs.getString("description"));

        bean.setMaxScore(rs.getBigDecimal("max_score"));
        bean.setWeightPercent(rs.getBigDecimal("weight_percent"));

        if (rs.getTimestamp("due_date") != null) {
            bean.setDueDate(rs.getTimestamp("due_date").toLocalDateTime());
        }

        bean.setStatus(rs.getString("status"));

        if (rs.getTimestamp("created_at") != null) {
            bean.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }

        if (rs.getTimestamp("updated_at") != null) {
            bean.setAssignupdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        }

        return bean;

    });

}

//get and view assignment student only when he join
public List<SubmissionBean> getStudentAssignments(String userID) {

    String sql = """
        SELECT
            a.assignmentID,
            a.courseID,
            c.name,
            a.createdByID,
            a.title,
            a.description,
            a.max_score,
            a.weight_percent,
            a.due_date,
            a.status,
            a.created_at,
            a.updated_at

        FROM assignment a

        JOIN course c
            ON a.courseID = c.courseID

        JOIN enrollment e
            ON a.courseID = e.courseID

        WHERE e.userID = ?
          AND a.status = 'Published'

        ORDER BY a.due_date ASC
        """;

    return jdbc.query(sql, (rs, rowNum) -> {

       SubmissionBean bean = new SubmissionBean();

        bean.setAssignmentID(rs.getString("assignmentID"));
        bean.setCourseID(rs.getString("courseID"));
        bean.setCourseName(rs.getString("name"));
        bean.setCreatedByID(rs.getString("createdByID"));

        bean.setTitle(rs.getString("title"));
        bean.setDescription(rs.getString("description"));

        bean.setMaxScore(rs.getBigDecimal("max_score"));
        bean.setWeightPercent(rs.getBigDecimal("weight_percent"));

        if (rs.getTimestamp("due_date") != null) {
            bean.setDueDate(rs.getTimestamp("due_date").toLocalDateTime());
        }

        bean.setStatus(rs.getString("status"));

        if (rs.getTimestamp("created_at") != null) {
            bean.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }

        if (rs.getTimestamp("updated_at") != null) {
            bean.setAssignupdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        }

        return bean;

    }, userID);

}

//Check whether the student has already submitted the assignment
public boolean hasSubmitted(String assignmentID, String userID) {

    String sql = """
            SELECT COUNT(*)
            FROM submission
            WHERE assignmentID = ?
              AND userID = ?
            """;

    Integer count = jdbc.queryForObject(
            sql,
            Integer.class,
            assignmentID,
            userID
    );

    return count != null && count > 0;
}

}



