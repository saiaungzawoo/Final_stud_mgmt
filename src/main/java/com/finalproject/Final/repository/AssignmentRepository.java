package com.finalproject.Final.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.AssignmentBean;
import com.finalproject.Final.model.AssignmentStatus;
import com.finalproject.Final.model.CourseBean;

@Repository
public class AssignmentRepository {

    @Autowired
    private JdbcTemplate jdbc;
    
    public List<CourseBean> getTeacherCourses(String teacherID){

        String sql = """
                SELECT
                    courseID,
                    name,
                    description
                FROM course
                WHERE teacherID=?
                ORDER BY name
                """;

        return jdbc.query(sql, (rs,rowNum)->{

            CourseBean obj = new CourseBean();

            obj.setCourseId(rs.getString("courseID"));
            obj.setName(rs.getString("name"));
            obj.setDescription(rs.getString("description"));

            return obj;

        }, teacherID);

    }
    public int saveAssignment(AssignmentBean obj){

        String sql = """
                INSERT INTO assignment
                (
                    assignmentID,
                    courseID,
                    createdByID,
                    title,
                    description,
                    max_score,
                    weight_percent,
                    due_date,
                    status
                )
                VALUES
                (?,?,?,?,?,?,?,?,?)
                """;

        return jdbc.update(
                sql,

                obj.getAssignmentID(),
                obj.getCourseID(),
                obj.getCreatedByID(),
                obj.getTitle(),
                obj.getDescription(),
                obj.getMaxScore(),
                obj.getWeightPercent(),
                obj.getDueDate(),
                obj.getStatus().name()
        );

    }
    public List<AssignmentBean> getAssignmentList(String teacherID){

        String sql = """
                SELECT
                    a.*,
                    c.name AS courseName
                FROM assignment a
                JOIN course c
                ON a.courseID = c.courseID
                WHERE a.createdByID=?
                ORDER BY a.created_at DESC
                """;

        return jdbc.query(sql, (rs,rowNum)->{

            AssignmentBean obj = new AssignmentBean();

            obj.setAssignmentID(rs.getString("assignmentID"));
            obj.setCourseID(rs.getString("courseID"));
            obj.setCreatedByID(rs.getString("createdByID"));

            obj.setTitle(rs.getString("title"));
            obj.setDescription(rs.getString("description"));

            obj.setMaxScore(rs.getBigDecimal("max_score"));
            obj.setWeightPercent(rs.getBigDecimal("weight_percent"));

            obj.setDueDate(
                    rs.getTimestamp("due_date").toLocalDateTime()
            );

            obj.setStatus(
                    AssignmentStatus.valueOf(
                            rs.getString("status")
                    )
            );

            // Bean ထဲမှာ private String courseName; ထည့်ထားရမယ်
            obj.setCourseName(rs.getString("courseName"));

            return obj;

        }, teacherID);

    }
    public AssignmentBean getAssignmentById(String assignmentID){

        String sql = """
                SELECT *
                FROM assignment
                WHERE assignmentID=?
                """;

        return jdbc.queryForObject(sql, (rs,rowNum)->{

            AssignmentBean obj = new AssignmentBean();

            obj.setAssignmentID(rs.getString("assignmentID"));
            obj.setCourseID(rs.getString("courseID"));
            obj.setCreatedByID(rs.getString("createdByID"));

            obj.setTitle(rs.getString("title"));
            obj.setDescription(rs.getString("description"));

            obj.setMaxScore(rs.getBigDecimal("max_score"));
            obj.setWeightPercent(rs.getBigDecimal("weight_percent"));

            obj.setDueDate(
                    rs.getTimestamp("due_date").toLocalDateTime()
            );

            obj.setStatus(
                    AssignmentStatus.valueOf(
                            rs.getString("status")
                    )
            );

            return obj;

        }, assignmentID);

    }
    public int updateAssignment(AssignmentBean obj){

        String sql = """
                UPDATE assignment
                SET
                    courseID=?,
                    title=?,
                    description=?,
                    max_score=?,
                    weight_percent=?,
                    due_date=?,
                    status=?
                WHERE assignmentID=?
                """;


        return jdbc.update(
                sql,

                obj.getCourseID(),
                obj.getTitle(),
                obj.getDescription(),
                obj.getMaxScore(),
                obj.getWeightPercent(),
                obj.getDueDate(),
                obj.getStatus().name(),
                obj.getAssignmentID()

        );

    }
    public List<AssignmentBean> getAssignmentListByCourse(String courseID) {


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
                    c.name AS courseName

                FROM assignment a

                JOIN course c
                ON a.courseID = c.courseID

                WHERE a.courseID = ?

                ORDER BY a.due_date DESC
                """;


        return jdbc.query(sql, (rs, rowNum) -> {


            AssignmentBean bean = new AssignmentBean();



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



            if(rs.getTimestamp("due_date") != null) {

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



            bean.setCourseName(
                    rs.getString("courseName")
            );



            return bean;


        }, courseID);

    }

}
