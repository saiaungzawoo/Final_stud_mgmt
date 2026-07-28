package com.finalproject.Final.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.ClassBean;

@Repository
public class ClassRepository {


    @Autowired
    private JdbcTemplate jdbcTemplate;



    public List<ClassBean> getTeacherClasses(String teacherID){


    	String sql = """

    			SELECT
    			    c.courseID,
    			    c.name,
    			    c.level,

    			    COUNT(DISTINCT e.userID) AS studentCount,


    			    GROUP_CONCAT(
    			        DISTINCT CONCAT(
    			            DAYNAME(s.schedule_date),
    			            '|',
    			            TIME_FORMAT(s.start_time,'%h:%i %p'),
    			            ' - ',
    			            TIME_FORMAT(s.end_time,'%h:%i %p')
    			        )
    			        ORDER BY DAYOFWEEK(s.schedule_date)
    			        SEPARATOR ','
    			    ) AS scheduleInfo


    			FROM course c


    			LEFT JOIN enrollment e
    			ON c.courseID = e.courseID
    			AND e.status = 'Active'


    			LEFT JOIN schedule s
    			ON c.courseID = s.courseID
    			AND s.status = 'Scheduled'


    			WHERE c.teacherID = ?


    			GROUP BY
    			    c.courseID,
    			    c.name,
    			    c.level

    			""";


        return jdbcTemplate.query(sql,
                (rs,rowNum)->{

                    ClassBean bean = new ClassBean();

                    bean.setCourseID(rs.getString("courseID"));
                    bean.setName(rs.getString("name"));
                    bean.setLevel(rs.getString("level"));
                    bean.setStudentCount(rs.getInt("studentCount"));
                    bean.setScheduleInfo(rs.getString("scheduleInfo"));

                    return bean;

                },
                teacherID
        );

    }

}