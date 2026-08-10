 package com.finalproject.Final.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.model.EnrollmentBean;
import com.finalproject.Final.model.FinalGradeBean;

@Repository
public class FinalGradeRepository {

    private final JdbcTemplate jdbcTemplate;

    public FinalGradeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    // Get active students by course
    public List<EnrollmentBean> getStudentByCourse(String courseId) {

        String sql = """
                SELECT
                    e.enrollmentID,
                    e.userID,
                    e.courseID,
                    e.enrollment_date,
                    e.status,
                    c.name AS courseTitle,
                    u.name AS username

                FROM enrollment e

                JOIN user u
                    ON e.userID = u.userID

                JOIN course c
                    ON e.courseID = c.courseID

                WHERE e.courseID = ?
                AND e.status = 'Active'

                ORDER BY u.name
                """;


        return jdbcTemplate.query(sql,
                (rs, rowNum) -> {

                    EnrollmentBean bean = new EnrollmentBean();

                    bean.setEnrollmentId(
                            rs.getString("enrollmentID")
                    );

                    bean.setUserId(
                            rs.getString("userID")
                    );

                    bean.setCourseId(
                            rs.getString("courseID")
                    );

                    bean.setEnrollmentDate(
                            rs.getDate("enrollment_date").toLocalDate()
                    );

                    bean.setStatus(
                            rs.getString("status")
                    );

                    bean.setCourseTitle(
                            rs.getString("courseTitle")
                    );

                    bean.setUsername(
                            rs.getString("username")
                    );


                    return bean;

                },
                courseId
        );
    }
    public Double calculateAssignmentScore(String enrollmentId) {

        String sql = """
                SELECT
                    (
                        SUM(
                            (
                                COALESCE(s.score,0) / a.max_score
                            )
                            * a.weight_percent
                        )
                        /
                        NULLIF(SUM(a.weight_percent),0)
                    ) * 30 AS total_score


                FROM enrollment e


                JOIN assignment a
                    ON e.courseID = a.courseID


                LEFT JOIN submission s
                    ON a.assignmentID = s.assignmentID
                    AND s.userID = e.userID


                WHERE e.enrollmentID = ?
                AND a.status = 'Closed'
                """;


        Double result = jdbcTemplate.queryForObject(
                sql,
                Double.class,
                enrollmentId
        );


        return result != null ? Math.round(result * 100.0) / 100.0
        	    : 0.0;
    }
    public Double calculateExamScore(String enrollmentId) {

        String sql = """
                SELECT
                    (
                        SUM(
                            (
                                COALESCE(er.score,0) / e.max_score
                            )
                            * e.weight_percent
                        )
                        /
                        NULLIF(SUM(e.weight_percent),0)
                    ) * 60 AS total_score


                FROM enrollment en


                JOIN exam e
                    ON en.courseID = e.courseID


                LEFT JOIN exam_result er
                    ON e.examID = er.examID
                    AND er.userID = en.userID


                WHERE en.enrollmentID = ?
                AND e.status = 'Completed'
                """;


        Double result = jdbcTemplate.queryForObject(
                sql,
                Double.class,
                enrollmentId
        );


        return result != null  ? Math.round(result * 100.0) / 100.0
        	    : 0.0;
    }
    public Double calculateAttendanceScore(String enrollmentId) {
String sql = """
                SELECT
                    (
                        SUM(
                            CASE 
                                WHEN a.status = 'Present' THEN 1
                                ELSE 0
                            END
                        )
                        /
                        COUNT(a.attendanceID)
                    ) * 10 AS attendance_score

                FROM enrollment e

                JOIN attendance a
                    ON e.userID = a.userID

                JOIN schedule s
                    ON a.scheduleID = s.scheduleID
                    AND e.courseID = s.courseID

                WHERE e.enrollmentID = ?
                """;


        Double result = jdbcTemplate.queryForObject(
                sql,
                Double.class,
                enrollmentId
        );


        return result != null ? result : 0.0;
    }
    public Double calculateFinalScore(
            Double assignmentScore,
            Double examScore,
            Double attendanceScore) {


        Double total= assignmentScore 
                + examScore 
                + attendanceScore;
        return Math.round(total * 100.0) / 100.0;
    }
    public String generateLetterGrade(Double finalScore) {


        if(finalScore >= 90) {
            return "A";
        }
        else if(finalScore >= 80) {
            return "B";
        }
        else if(finalScore >= 70) {
            return "C";
        }
        else if(finalScore >= 60) {
            return "D";
        }
        else {
            return "F";
        }

    }
    public String generateStatus(Double finalScore) {


        if(finalScore >= 60) {

            return "Completed";

        } else {

            return "Failed";

        }

    }
    public int saveFinalGrade(FinalGradeBean bean) {

        String sql = """
                INSERT INTO final_grade
                (
                    finalGradeID,
                    enrollmentID,
                    assignment_total_score,
                    exam_total_score,
                    attendance_score,
                    final_score,
                    letter_grade,
                    status,
                    remarks,
                    finalizedByID,
                    finalized_at,
                    created_at,
                    updated_at
                )
                VALUES
                (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())
                """;


        return jdbcTemplate.update(
                sql,

                bean.getFinalGradeID(),

                bean.getEnrollmentID(),

                bean.getAssignmentTotalScore(),

                bean.getExamTotalScore(),

                bean.getAttendanceScore(),

                bean.getFinalScore(),

                bean.getLetterGrade(),

                bean.getStatus(),

                bean.getRemarks(),

                bean.getFinalizedByID(),

                bean.getFinalizedAt()
        );
    }
    public List<CourseBean> getTeacherCourses(String teacherId) {

        String sql = """
                SELECT
                    c.courseID,
                    c.name,
                    c.description,
                    c.duration_weeks,
                    c.fee,
                    c.level,
                    c.status,
                    c.teacherID,
                    u.name AS teacherName

                FROM course c

                JOIN user u
                    ON c.teacherID = u.userID

                WHERE c.teacherID = ?
                AND c.is_active = 1 
                AND c.status IN ('Open', 'In Progress')

                ORDER BY c.name
                """;


        return jdbcTemplate.query(sql,
                (rs, rowNum) -> {

                    CourseBean bean = new CourseBean();

                    bean.setCourseId(rs.getString("courseID"));
                    bean.setName(rs.getString("name"));
                    bean.setDescription(rs.getString("description"));

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

                    bean.setTeacherId(
                            rs.getString("teacherID")
                    );

                    bean.setTeacherName(
                            rs.getString("teacherName")
                    );


                    return bean;
                },
                teacherId
        );
    }
    public FinalGradeBean calculateFinalGrade(String enrollmentId) {
      System.out.println("SEARCH ENROLLMENT ID = " + enrollmentId);

        String sql = """
                SELECT
                    finalGradeID,
                    enrollmentID,
                    assignment_total_score,
                    exam_total_score,
                    attendance_score,
                    final_score,
                    letter_grade,
                    status,
                    remarks,
                    finalizedByID,
                    finalized_at

                FROM final_grade

                WHERE enrollmentID = ?
                """;
       

        List<FinalGradeBean> list =
                jdbcTemplate.query(sql, (rs,rowNum)->{
               

            FinalGradeBean bean = new FinalGradeBean();


            bean.setFinalGradeID(
                    rs.getString("finalGradeID")
            );

            bean.setEnrollmentID(
                    rs.getString("enrollmentID")
            );


            bean.setAssignmentTotalScore(
                    rs.getDouble("assignment_total_score")
            );


            bean.setExamTotalScore(
                    rs.getDouble("exam_total_score")
            );


            bean.setAttendanceScore(
                    rs.getDouble("attendance_score")
            );


            bean.setFinalScore(
                    rs.getDouble("final_score")
            );


            bean.setLetterGrade(
                    rs.getString("letter_grade")
            );


            bean.setStatus(
                    rs.getString("status")
            );


            bean.setRemarks(
                    rs.getString("remarks")
            );


            return bean;


        }, enrollmentId);



        // Finalize လုပ်ပြီးသားရှိရင် database data ပြန်ပေး
        if(!list.isEmpty()) {
            return list.get(0);
        }

       System.out.println("FINAL GRADE COUNT = " + list.size());

        // မ finalize ရသေးရင် calculate လုပ်
        Double assignmentScore =
                calculateAssignmentScore(enrollmentId);


        Double examScore =
                calculateExamScore(enrollmentId);


        Double attendanceScore =
                calculateAttendanceScore(enrollmentId);



        Double finalScore =
                calculateFinalScore(
                        assignmentScore,
                        examScore,
                        attendanceScore
                );



        FinalGradeBean bean = new FinalGradeBean();


        bean.setEnrollmentID(enrollmentId);

        bean.setAssignmentTotalScore(assignmentScore);

        bean.setExamTotalScore(examScore);

        bean.setAttendanceScore(attendanceScore);

        bean.setFinalScore(finalScore);

        bean.setLetterGrade(
                generateLetterGrade(finalScore)
        );

        bean.setStatus(
                generateStatus(finalScore)
        );


        return bean;
    }
    public boolean existsByEnrollmentId(String enrollmentId) {

        String sql = """
                SELECT COUNT(*)
                FROM final_grade
                WHERE enrollmentID = ?
                """;


        Integer count = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                enrollmentId
        );


        return count != null && count > 0;
    }
    public List<FinalGradeBean> getAllFinalGrades(){
String sql = """
                
                SELECT
                    fg.finalGradeID,
                    fg.enrollmentID,

                    u.name AS studentName,
                    c.name AS courseName,

                    fg.assignment_total_score,
                    fg.exam_total_score,
                    fg.attendance_score,

                    fg.final_score,
                    fg.letter_grade,
                    fg.status,

                    fg.finalized_at

                FROM final_grade fg

                JOIN enrollment e
                ON fg.enrollmentID = e.enrollmentID

                JOIN user u
                ON e.userID = u.userID

                JOIN course c
                ON e.courseID = c.courseID

                ORDER BY fg.created_at DESC

                """;


        return jdbcTemplate.query(sql, (rs,rowNum)->{

            FinalGradeBean bean = new FinalGradeBean();


            bean.setFinalGradeID(
                    rs.getString("finalGradeID"));

            bean.setEnrollmentID(
                    rs.getString("enrollmentID"));


            bean.setStudentName(
                    rs.getString("studentName"));

            bean.setCourseName(
                    rs.getString("courseName"));


            bean.setAssignmentTotalScore(
                    rs.getDouble("assignment_total_score"));

            bean.setExamTotalScore(
                    rs.getDouble("exam_total_score"));

            bean.setAttendanceScore(
                    rs.getDouble("attendance_score"));


            bean.setFinalScore(
                    rs.getDouble("final_score"));

            bean.setLetterGrade(
                    rs.getString("letter_grade"));

            bean.setStatus(
                    rs.getString("status"));


            return bean;

        });

    }
    public boolean existsByEnrollmentID(String enrollmentID){

        String sql = """
                SELECT COUNT(*)
                FROM final_grade
                WHERE enrollmentID = ?
                """;

        Integer count = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                enrollmentID
        );

        return count > 0;
    }
    //use for admin view  add thiri
    public List<FinalGradeBean> getAllCourses() {

        String sql = """
            SELECT
                c.courseID,
                c.name,
                c.teacherID,
                u.name AS teacherName,
                COUNT(e.enrollmentID) AS studentCount
            FROM course c
            LEFT JOIN user u
                ON c.teacherID = u.userID
            LEFT JOIN enrollment e
                ON c.courseID = e.courseID
                AND e.status = 'Active'
            GROUP BY
                c.courseID,
                c.name,
                c.teacherID,
                u.name
            ORDER BY c.name
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {

           FinalGradeBean bean = new FinalGradeBean();

            bean.setCourseID(rs.getString("courseID"));
            bean.setCourseName(rs.getString("name"));
            bean.setTeacherID(rs.getString("teacherID"));
            bean.setTeacherName(rs.getString("teacherName"));
            bean.setStudentCount(rs.getInt("studentCount"));

            return bean;
        });
    }
   public List<FinalGradeBean> getFinalGradesByCourse(String courseId) {

        String sql = """
            SELECT
                fg.finalGradeID,
                fg.final_score,
                fg.letter_grade,
                fg.status,
                fg.finalized_at,

                u.name AS studentName,
                c.name AS courseName

            FROM final_grade fg

            JOIN enrollment e
                ON fg.enrollmentID = e.enrollmentID

            JOIN user u
                ON e.userID = u.userID

            JOIN course c
                ON e.courseID = c.courseID

            WHERE c.courseID = ?

            ORDER BY u.name
            """;
 return jdbcTemplate.query(sql,(rs,row)->{

            FinalGradeBean bean = new FinalGradeBean();

            bean.setFinalGradeID(rs.getString("finalGradeID"));
            bean.setStudentName(rs.getString("studentName"));
            bean.setCourseName(rs.getString("courseName"));
            bean.setFinalScore(rs.getDouble("final_score"));
            bean.setLetterGrade(rs.getString("letter_grade"));
            bean.setStatus(rs.getString("status"));
            Timestamp ts = rs.getTimestamp("finalized_at");

            if (ts != null) {
                bean.setFinalizedAt(ts.toLocalDateTime());
            }
            return bean;

        },courseId);
    }
}