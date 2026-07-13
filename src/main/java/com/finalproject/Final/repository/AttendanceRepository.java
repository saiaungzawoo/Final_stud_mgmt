package com.finalproject.Final.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.AttendanceBean;
import com.finalproject.Final.model.AttendanceBean.AttendanceStatus;
import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.model.ScheduleBean;
@Repository
public class AttendanceRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<AttendanceBean> getStudentList(String courseID,String scheduleID){

        String sql="""
                SELECT
                u.userID,
                u.name
                FROM enrollment e
                JOIN user u
                ON e.userID=u.userID
                WHERE e.courseID=?
                AND e.status='Active'
                ORDER BY u.name
                """;

        return jdbcTemplate.query(sql,(rs,rowNum)->{

            AttendanceBean obj=new AttendanceBean();

            obj.setUserID(rs.getString("userID"));
            obj.setStudentName(rs.getString("name"));
            obj.setScheduleID(scheduleID);

            return obj;

        },courseID);

    }
    public int saveAttendance(AttendanceBean obj) {

        System.out.println("Saving...");
        System.out.println(obj.getUserID());
        System.out.println(obj.getScheduleID());
        System.out.println(obj.getStatus());

        String sql = """
            INSERT INTO attendance(
            attendanceID,
            scheduleID,
            userID,
            status,
            check_in_time,
            remarks,
            markedByID
            )
            VALUES(UUID(),?,?,?,?,?,?)
            """;

        int result = jdbcTemplate.update(
                sql,
                obj.getScheduleID(),
                obj.getUserID(),
                obj.getStatus().name(),
                obj.getCheckInTime(),
                obj.getRemarks(),
                "00ee5b4b-7a6f-11f1-8f4f-183d2d227d02"
        );

        System.out.println("Insert Result = " + result);

        return result;
    }
    public List<CourseBean> getTeacherCourses(String teacherID) {

        String sql = """
                SELECT
                    courseID,
                    name,
                    description
                FROM course
                WHERE teacherID = ?
                ORDER BY name
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            CourseBean obj = new CourseBean();

            obj.setCourseId(rs.getString("courseID"));
            obj.setName(rs.getString("name"));
            obj.setDescription(rs.getString("description"));

            return obj;

        }, teacherID);

    }
    

    public List<ScheduleBean> getScheduleByCourse(String courseID){

        String sql = """
                SELECT
                    scheduleID,
                    courseID,
                    schedule_date,
                    start_time,
                    end_time,
                    room,
                    topic,
                    status
                FROM schedule
                WHERE courseID=?
                ORDER BY schedule_date,start_time
                """;

        return jdbcTemplate.query(sql,(rs,rowNum)->{

            ScheduleBean obj=new ScheduleBean();

            obj.setScheduleId(rs.getString("scheduleID"));
            obj.setCourseId(rs.getString("courseID"));
            obj.setScheduleDate(rs.getDate("schedule_date").toLocalDate());
            obj.setStartTime(rs.getTime("start_time").toLocalTime());
            obj.setEndTime(rs.getTime("end_time").toLocalTime());
            obj.setRoom(rs.getString("room"));
            obj.setTopic(rs.getString("topic"));
            obj.setStatus(rs.getString("status"));
            obj.setAttendanceMarked(
                    isAttendanceMarked(
                        rs.getString("scheduleID")
                    )
            );
            return obj;

        },courseID);

    }
    public boolean attendanceExists(String scheduleID,String userID){

        String sql="""
            SELECT COUNT(*)
            FROM attendance
            WHERE scheduleID=?
            AND userID=?
            """;

        Integer count=jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                scheduleID,
                userID);

        return count!=null && count>0;

    }
    public int updateAttendance(AttendanceBean obj){

        String sql="""
            UPDATE attendance
            SET
                status=?,
                check_in_time=?,
                remarks=?,
                markedByID=?
            WHERE scheduleID=?
            AND userID=?
            """;

        return jdbcTemplate.update(
                sql,
                obj.getStatus().name(),
                obj.getCheckInTime(),
                obj.getRemarks(),
                "00ee5b4b-7a6f-11f1-8f4f-183d2d227d02",
                obj.getScheduleID(),
                obj.getUserID()
        );

    }
    public boolean isAttendanceMarked(String scheduleID){

        String sql = """
                SELECT COUNT(*)
                FROM attendance
                WHERE scheduleID=?
                """;


        Integer count = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                scheduleID
        );


        return count != null && count > 0;

    }
    public List<AttendanceBean> getStudentAttendanceHistory(String userID){


        String sql = """
            SELECT
                s.schedule_date,
                s.topic,
                s.start_time,
                s.end_time,
                a.status,
                a.remarks

            FROM attendance a

            JOIN schedule s
            ON a.scheduleID = s.scheduleID

            WHERE a.userID = ?

            ORDER BY s.schedule_date ASC
            """;


        return jdbcTemplate.query(sql,(rs,rowNum)->{


            AttendanceBean obj = new AttendanceBean();


            obj.setStudentName(rs.getString("topic"));


            obj.setStatus(
                AttendanceStatus.valueOf(
                    rs.getString("status")
                )
            );


            obj.setRemarks(
                rs.getString("remarks")
            );


            obj.setScheduleDate(
                rs.getDate("schedule_date")
                .toLocalDate()
            );


            obj.setStartTime(
                rs.getTime("start_time")
                .toLocalTime()
            );


            obj.setEndTime(
                rs.getTime("end_time")
                .toLocalTime()
            );


            return obj;


        },userID);

    }
    public List<AttendanceBean> getStudentListByCourse(String courseID){


        String sql = """
            SELECT
                u.userID,
                u.name

            FROM enrollment e

            JOIN user u
            ON e.userID = u.userID

            WHERE e.courseID=?
            AND e.status='Active'

            ORDER BY u.name
            """;


        return jdbcTemplate.query(
            sql,
            (rs,rowNum)->{


                AttendanceBean obj =
                        new AttendanceBean();


                obj.setUserID(
                        rs.getString("userID")
                );


                obj.setStudentName(
                        rs.getString("name")
                );


                return obj;


            },
            courseID
        );

    }
}