package com.finalproject.Final.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.ScheduleBean;
import com.finalproject.Final.model.TeacherBean;


@Repository
public class TeacherRepository {

    @Autowired
    private JdbcTemplate jdbc;


    // Insert Teacher
    public int insertTeacher(TeacherBean obj) {

        String sql = """
                INSERT INTO user
                (userID, roleID, name, email, password, phone_no, 
                 address, dob, gender, created_at, is_active, profile_image)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, ?)
                """;


        return jdbc.update(
                sql,
                obj.getUserID(),
                obj.getRoleID(),
                obj.getName(),
                obj.getEmail(),
                obj.getPassword(),
                obj.getPhoneNo(),
                obj.getAddress(),
                obj.getDob(),
                obj.getGender(),
                obj.getIsActive(),
                obj.getProfileImage()
        );
    }



    // Get All Teacher
    public List<TeacherBean> getAllTeacher() {

        String sql = """
                SELECT *
                FROM `user`
                WHERE roleID = ?
                """;


        return jdbc.query(
                sql,

                (rs,rowNum)-> new TeacherBean(
                        rs.getString("userID"),
                        rs.getString("roleID"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone_no"),
                        rs.getString("address"),
                        rs.getString("dob"),
                        rs.getString("gender"),
                        rs.getString("profile_image"),
                        rs.getInt("is_active"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                ),

               // "00ec67a1-7a6f-11f1-8f4f-183d2d227d02"
                "3c2f3f12-7a84-11f1-bfcb-b4b686e7f920"
        );
    }


    // Get Teacher By ID
    public TeacherBean getByTeacherId(String userID) {


        String sql = """
                SELECT *
                FROM user
                WHERE userID = ?
                """;


        return jdbc.queryForObject(
                sql,

                (rs, rowNum) -> new TeacherBean(

                        rs.getString("userID"),

                        rs.getString("roleID"),

                        rs.getString("name"),

                        rs.getString("email"),

                        rs.getString("password"),

                        rs.getString("phone_no"),

                        rs.getString("address"),

                        rs.getString("dob"),

                        rs.getString("gender"),

                        rs.getString("profile_image"),

                        rs.getInt("is_active"),

                        rs.getTimestamp("created_at"),

                        rs.getTimestamp("updated_at")

                ),

                userID
        );
    }





    // Update Teacher
    public int updateUpload(TeacherBean obj) {


        String sql = """
                UPDATE user
                SET
                    name=?,
                    email=?,
                    password=?,
                    phone_no=?,
                    address=?,
                    dob=?,
                    gender=?,
                    is_active=?,
                    profile_image=?,
                    updated_at=NOW()
                WHERE userID=?
                """;


        return jdbc.update(
                sql,

                obj.getName(),
                obj.getEmail(),
                obj.getPassword(),
                obj.getPhoneNo(),
                obj.getAddress(),
                obj.getDob(),
                obj.getGender(),
                obj.getIsActive(),
                obj.getProfileImage(),
                obj.getUserID()

        );
    }
    public int countClasses(String teacherID) {

        String sql = """
                SELECT COUNT(*)
                FROM course
                WHERE teacherID = ?
                """;


        return jdbc.queryForObject(
                sql,
                Integer.class,
                teacherID
        );
    }




    // =========================
    // Assignment Count
    // =========================

    public int countAssignments(String teacherID) {


        String sql = """
                SELECT COUNT(*)
                FROM assignment
                WHERE createdByID = ?
                """;


        return jdbc.queryForObject(
                sql,
                Integer.class,
                teacherID
        );
    }





    // =========================
    // Pending Submission
    // =========================

    public int countPendingSubmission(String teacherID) {


        String sql = """
                SELECT COUNT(*)
                FROM submission s
                JOIN assignment a
                ON s.assignmentID = a.assignmentID
                WHERE a.createdByID = ?
                AND s.score IS NULL
                """;


        return jdbc.queryForObject(
                sql,
                Integer.class,
                teacherID
        );

    }






    // =========================
    // Today Attendance %
    // =========================

    public int todayAttendancePercent(String teacherID) {


        String sql = """

        SELECT 
        CASE 
        WHEN COUNT(a.attendanceID)=0 THEN 0

        ELSE ROUND(
        SUM(
        CASE 
        WHEN a.status='Present'
        THEN 1 ELSE 0 END
        )
        *100 / COUNT(a.attendanceID)
        )

        END

        FROM attendance a

        JOIN schedule s
        ON a.scheduleID=s.scheduleID

        JOIN course c
        ON s.courseID=c.courseID

        WHERE c.teacherID=?

        AND s.schedule_date = CURDATE()

        """;


        return jdbc.queryForObject(
                sql,
                Integer.class,
                teacherID
        );
    }







    // =========================
    // Today's Schedule
    // =========================

    public List<ScheduleBean> getTodaySchedule(
            String teacherID) {


        String sql = """

        SELECT 
        s.scheduleID,
        s.courseID,
        c.name AS courseName,
        s.schedule_date,
        s.start_time,
        s.end_time,
        s.room,
        s.topic

        FROM schedule s

        JOIN course c
        ON s.courseID=c.courseID

        WHERE c.teacherID=?

        AND s.schedule_date = CURDATE()

        ORDER BY s.start_time

        """;


        return jdbc.query(
                sql,
                (rs,rowNum)->{


                    ScheduleBean obj =
                            new ScheduleBean();


                    obj.setScheduleId(
                            rs.getString("scheduleID")
                    );


                    obj.setCourseId(
                            rs.getString("courseID")
                    );


                    obj.setCourseName(
                            rs.getString("courseName")
                    );

                    obj.setStartTime(
                    	    rs.getTime("start_time").toLocalTime()
                    	);


                    	obj.setEndTime(
                    	    rs.getTime("end_time").toLocalTime()
                    	);


                    obj.setRoom(
                            rs.getString("room")
                    );


                    obj.setTopic(
                            rs.getString("topic")
                    );


                    return obj;

                },
                teacherID
        );

    }






    // =========================
    // Recent Announcement
    // =========================

    public List<String> getRecentAnnouncements(
            String teacherID){


        String sql = """

        SELECT title

        FROM announcement

        WHERE createdByID=?

        ORDER BY created_at DESC

        LIMIT 5

        """;


        return jdbc.query(
                sql,
                (rs,rowNum)
                ->
                rs.getString("title"),
                teacherID
        );

    }



}