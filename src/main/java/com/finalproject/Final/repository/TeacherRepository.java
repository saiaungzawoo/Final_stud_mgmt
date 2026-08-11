package com.finalproject.Final.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.AnnouncementBean;
import com.finalproject.Final.model.ScheduleBean;
import com.finalproject.Final.model.TeacherBean;


@Repository
public class TeacherRepository {

    @Autowired
    private JdbcTemplate jdbc;


    // Insert Teacher
    //sai
    //I added user code, dont delete this
    //nothing is broken
    public int insertTeacher(TeacherBean obj) {

        String sql = """
                INSERT INTO user
                (userID, roleID, userCode, name, email, password, phone_no, 
                 address, dob, gender, created_at, is_active, profile_image)
                VALUES (?, ?, ?, ?,	 ?, ?, ?, ?, ?, ?, NOW(), ?, ?)
                """;


        return jdbc.update(
                sql,
                obj.getUserID(),
                obj.getRoleID(),
                obj.getUserCode(),
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


//i didnt delete anything//sai
    // Get All Teacher
//    public List<TeacherBean> getAllTeacher() {
//
//        String sql = """
//                SELECT *
//                FROM `user`
//                WHERE roleID = ?
//                """;
//
//
//        return jdbc.query(
//                sql,
//
//                (rs,rowNum)-> new TeacherBean(
//                        rs.getString("userID"),
//                        rs.getString("roleID"),
//                        rs.getString("name"),
//                        rs.getString("email"),
//                        rs.getString("password"),
//                        rs.getString("phone_no"),
//                        rs.getString("address"),
//                        rs.getString("dob"),
//                        rs.getString("gender"),
//                        rs.getString("profile_image"),
//                        rs.getInt("is_active"),
//                        rs.getTimestamp("created_at"),
//                        rs.getTimestamp("updated_at")
//                ),
//
//               // "00ec67a1-7a6f-11f1-8f4f-183d2d227d02"
//                "19dac071-7acd-11f1-898e-e4b97a5cf834"
    
    //sai
    //hard coding UUID is not good. use role name instead 
//        );
//    }
    
    //sai
  //hard coding UUID is not good. use role name instead 
    public List<TeacherBean> getAllTeacher() {


        String sql = """
                SELECT u.*
                FROM user u
                JOIN role r
                ON u.roleID = r.roleID
                WHERE r.name = 'Teacher'
                """;


        return jdbc.query(
                sql,

                (rs,rowNum)-> new TeacherBean(

                        rs.getString("userID"),

                        rs.getString("roleID"),
                        
                        rs.getString("userCode"),

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
                )
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
                        
                        rs.getString("userCode"),

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
                AND is_active = 1
AND status IN ('Open', 'In Progress')
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
        AND c.is_active = 1
AND c.status IN ('Open', 'In Progress')

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
        AND c.is_active = 1
AND c.status IN ('Open', 'In Progress')

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

    public List<AnnouncementBean> getRecentAnnouncements(String teacherID) {


    	String sql = """

    			SELECT
    			    a.announcementID,
    			    a.createdByID,
    			    a.courseID,
    			    c.name AS courseName,
    			    a.title,
    			    a.content,
    			    a.target_type,
    			    a.priority,
    			    a.is_published,
    			    a.publish_date,
    			    a.expiry_date,
    			    a.created_at,
    			    a.updated_at

    			FROM announcement a

    			LEFT JOIN course c
    			ON a.courseID = c.courseID

    			WHERE a.createdByID = ?

    			ORDER BY a.created_at DESC

    			LIMIT 3

    			""";


        return jdbc.query(
            sql,
            (rs,rowNum)->{


                AnnouncementBean obj =
                        new AnnouncementBean();


                obj.setAnnouncementID(
                    rs.getString("announcementID")
                );


                obj.setCreatedByID(
                    rs.getString("createdByID")
                );


                obj.setCourseID(
                    rs.getString("courseID")
                );


                obj.setCourseName(
                    rs.getString("courseName")
                );


                obj.setTitle(
                    rs.getString("title")
                );


                obj.setContent(
                    rs.getString("content")
                );


                obj.setTargetType(
                    rs.getString("target_type")
                );


                obj.setPriority(
                    rs.getString("priority")
                );


                obj.setPublished(
                    rs.getBoolean("is_published")
                );


                obj.setPublishDate(
                    rs.getTimestamp("publish_date")
                    .toLocalDateTime()
                );


                obj.setCreatedAt(
                    rs.getTimestamp("created_at")
                    .toLocalDateTime()
                );


                return obj;

            },
            teacherID
        );

    }
    
//thiri
    public String getRoleIdByName(String roleName) {

        String sql = "SELECT roleID FROM role WHERE name = ?";

        return jdbc.queryForObject(sql, String.class, roleName);
    }

}