package com.finalproject.Final.repository;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.finalproject.Final.model.ExamBean;
import com.finalproject.Final.model.ExamResultBean;

@Repository
public class StudentExamRepository {

    @Autowired
    private JdbcTemplate jdbc;

    // 1. Student Enrolled လုပ်ထားသော သင်တန်းများမှ Scheduled / Completed Exam များအားလုံးကို ကြည့်ခြင်း
    public List<ExamBean> getStudentExams(String userID) {
        String sql = """
            SELECT 
                e.examID,
                e.courseID,
                c.name AS courseName,
                e.name,
                e.exam_type,
                e.max_score,
                e.weight_percent,
                e.exam_date,
                e.duration_minutes,
                e.status
            FROM exam e
            JOIN course c ON e.courseID = c.courseID
            JOIN enrollment en ON e.courseID = en.courseID
            WHERE en.userID = ? 
              AND e.status IN ('Scheduled', 'Completed')
            ORDER BY e.exam_date ASC
            """;

        return jdbc.query(sql, (rs, rowNum) -> {
            ExamBean exam = new ExamBean();
            exam.setExamID(rs.getString("examID"));
            exam.setCourseID(rs.getString("courseID"));
            exam.setCourseName(rs.getString("courseName"));
            exam.setName(rs.getString("name"));
            exam.setExamType(rs.getString("exam_type"));
            exam.setMaxScore(rs.getBigDecimal("max_score"));
            exam.setWeightPercent(rs.getBigDecimal("weight_percent"));

            if (rs.getTimestamp("exam_date") != null) {
                exam.setExamDate(rs.getTimestamp("exam_date").toLocalDateTime());
            }

            exam.setDurationMinutes(rs.getInt("duration_minutes"));
            exam.setStatus(rs.getString("status"));

            return exam;
        }, userID);
    }

    // 2. Exam တစ်ခုချင်းစီ၏ Detail (အသေးစိတ်) ကို ကြည့်ခြင်း
    public ExamBean getExamByID(String examID) {
        String sql = """
            SELECT 
                e.examID,
                e.courseID,
                c.name AS courseName,
                e.name,
                e.exam_type,
                e.max_score,
                e.weight_percent,
                e.exam_date,
                e.duration_minutes,
                e.status
            FROM exam e
            JOIN course c ON e.courseID = c.courseID
            WHERE e.examID = ?
            """;

        return jdbc.queryForObject(sql, (rs, rowNum) -> {
            ExamBean exam = new ExamBean();
            exam.setExamID(rs.getString("examID"));
            exam.setCourseID(rs.getString("courseID"));
            exam.setCourseName(rs.getString("courseName"));
            exam.setName(rs.getString("name"));
            exam.setExamType(rs.getString("exam_type"));
            exam.setMaxScore(rs.getBigDecimal("max_score"));
            exam.setWeightPercent(rs.getBigDecimal("weight_percent"));

            if (rs.getTimestamp("exam_date") != null) {
                exam.setExamDate(rs.getTimestamp("exam_date").toLocalDateTime());
            }

            exam.setDurationMinutes(rs.getInt("duration_minutes"));
            exam.setStatus(rs.getString("status"));

            return exam;
        }, examID);
    }

    // 3. Validation: ကျောင်းသားသည် အဆိုပါ Course ထဲတွင် Enrollment ရှိမရှိ စစ်ဆေးခြင်း
    public boolean isEnrolledInCourse(String examID, String userID) {
        String sql = """
            SELECT COUNT(*) 
            FROM exam e
            JOIN enrollment en ON e.courseID = en.courseID
            WHERE e.examID = ? AND en.userID = ?
            """;
        Integer count = jdbc.queryForObject(sql, Integer.class, examID, userID);
        return count != null && count > 0;
    }

    // 4. Offline ဖြေဆိုပြီးသော ရလဒ်များ (Result) ကို ပြန်လည်ကြည့်ရှုခြင်း
    public List<ExamResultBean> getStudentResults(String userID) {
        String sql = """
            SELECT 
                er.examResultID,
                er.score AS obtainedScore,
                er.remarks,
                er.graded_at,
                e.examID,
                e.name,
                e.exam_type,
                e.max_score,
                c.name AS courseName
            FROM exam_result er
            JOIN exam e ON er.examID = e.examID
            JOIN course c ON e.courseID = c.courseID
            WHERE er.userID = ?
            ORDER BY er.graded_at DESC
            """;

        return jdbc.query(sql, (rs, rowNum) -> {
            ExamResultBean exam = new ExamResultBean();
            exam.setExamResultID(rs.getString("examResultID"));
            exam.setScore(rs.getBigDecimal("obtainedScore"));
            exam.setRemarks(rs.getString("remarks"));
            
            if (rs.getTimestamp("graded_at") != null) {
                exam.setGradedAt(rs.getTimestamp("graded_at").toLocalDateTime());
            }

            exam.setExamID(rs.getString("examID"));
            exam.setExamName(rs.getString("name"));
            exam.setExamType(rs.getString("exam_type"));
            exam.setMaxScore(rs.getBigDecimal("max_score"));
            exam.setCourseName(rs.getString("courseName"));

            return exam;
        }, userID);
    }
}