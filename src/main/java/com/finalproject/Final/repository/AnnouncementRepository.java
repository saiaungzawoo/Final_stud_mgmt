package com.finalproject.Final.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.AnnouncementBean;
import com.finalproject.Final.model.CourseBean;

@Repository
public class AnnouncementRepository {

    private final JdbcTemplate jdbcTemplate;

    public AnnouncementRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    // Teacher ရဲ့ Course List
    public List<CourseBean> getTeacherCourses(String teacherID) {

        String sql = """
                SELECT *
                FROM course
                WHERE teacherID = ?
                ORDER BY name
                """;

        return jdbcTemplate.query(sql, new RowMapper<CourseBean>() {

            @Override
            public CourseBean mapRow(ResultSet rs, int rowNum) throws SQLException {

                CourseBean bean = new CourseBean();

                bean.setCourseId(rs.getString("courseID"));
                bean.setName(rs.getString("name"));
                bean.setDescription(rs.getString("description"));

                return bean;
            }
        }, teacherID);
    }


    // Save Announcement
    public void saveAnnouncement(AnnouncementBean bean) {

        String sql = """
                INSERT INTO announcement
                (
                    announcementID,
                    createdByID,
                    courseID,
                    title,
                    content,
                    target_type,
                    priority,
                    is_published,
                    publish_date,
                    expiry_date
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;


        jdbcTemplate.update(sql,

                UUID.randomUUID().toString(),

                bean.getCreatedByID(),

                bean.getCourseID(),

                bean.getTitle(),

                bean.getContent(),

                bean.getTargetType(),

                bean.getPriority(),

                bean.isPublished(),

                bean.getPublishDate(),

                bean.getExpiryDate()
        );
    }
    public List<AnnouncementBean> getTeacherAnnouncements(String teacherID) {

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

                WHERE 
                    a.createdByID = ?
                    OR
                    a.target_type IN ('ALL','ALL_TEACHERS')

                ORDER BY a.created_at DESC
                """;


        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            AnnouncementBean bean = new AnnouncementBean();

            bean.setAnnouncementID(
                    rs.getString("announcementID")
            );

            bean.setCreatedByID(
                    rs.getString("createdByID")
            );

            bean.setCourseID(
                    rs.getString("courseID")
            );

            bean.setTitle(
                    rs.getString("title")
            );

            bean.setContent(
                    rs.getString("content")
            );

            bean.setTargetType(
                    rs.getString("target_type")
            );

            bean.setPriority(
                    rs.getString("priority")
            );

            bean.setPublished(
                    rs.getBoolean("is_published")
            );

            bean.setPublishDate(
                    rs.getTimestamp("publish_date") != null 
                    ? rs.getTimestamp("publish_date").toLocalDateTime()
                    : null
            );

            bean.setExpiryDate(
                    rs.getTimestamp("expiry_date") != null
                    ? rs.getTimestamp("expiry_date").toLocalDateTime()
                    : null
            );

            bean.setCreatedAt(
                    rs.getTimestamp("created_at").toLocalDateTime()
            );

            bean.setUpdatedAt(
                    rs.getTimestamp("updated_at").toLocalDateTime()
            );


            return bean;

        }, teacherID);
    }
    public AnnouncementBean findById(String announcementID) {

        String sql = """
                SELECT 
                    a.*,
                    c.name AS courseName
                FROM announcement a
                LEFT JOIN course c
                ON a.courseID = c.courseID
                WHERE a.announcementID = ?
                """;


        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {

            AnnouncementBean bean = new AnnouncementBean();

            bean.setAnnouncementID(
                    rs.getString("announcementID")
            );

            bean.setCreatedByID(
                    rs.getString("createdByID")
            );

            bean.setCourseID(
                    rs.getString("courseID")
            );

            bean.setCourseName(
                    rs.getString("courseName")
            );

            bean.setTitle(
                    rs.getString("title")
            );

            bean.setContent(
                    rs.getString("content")
            );

            bean.setTargetType(
                    rs.getString("target_type")
            );

            bean.setPriority(
                    rs.getString("priority")
            );

            bean.setPublished(
                    rs.getBoolean("is_published")
            );

            bean.setPublishDate(
                    rs.getTimestamp("publish_date") != null
                    ? rs.getTimestamp("publish_date").toLocalDateTime()
                    : null
            );

            bean.setExpiryDate(
                    rs.getTimestamp("expiry_date") != null
                    ? rs.getTimestamp("expiry_date").toLocalDateTime()
                    : null
            );

            return bean;

        }, announcementID);
    }
    public void updateAnnouncement(AnnouncementBean bean) {

        String sql = """
                UPDATE announcement
                SET
                    courseID = ?,
                    title = ?,
                    content = ?,
                    target_type = ?,
                    priority = ?,
                    is_published = ?,
                    expiry_date = ?,
                    updated_at = NOW()

                WHERE announcementID = ?
                """;


        jdbcTemplate.update(sql,

                bean.getCourseID(),

                bean.getTitle(),

                bean.getContent(),

                bean.getTargetType(),

                bean.getPriority(),

                bean.isPublished(),

                bean.getExpiryDate(),

                bean.getAnnouncementID()
        );
    }
    public List<AnnouncementBean> getAnnouncementsByCourse(String courseID) {

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
            JOIN course c
            ON a.courseID = c.courseID
            WHERE a.courseID = ?
            ORDER BY a.publish_date DESC
            """;


        return jdbcTemplate.query(sql, (rs, rowNum) -> {

            AnnouncementBean bean = new AnnouncementBean();

            bean.setAnnouncementID(rs.getString("announcementID"));
            bean.setCreatedByID(rs.getString("createdByID"));
            bean.setCourseID(rs.getString("courseID"));
            bean.setCourseName(rs.getString("courseName"));

            bean.setTitle(rs.getString("title"));
            bean.setContent(rs.getString("content"));

            bean.setTargetType(rs.getString("target_type"));
            bean.setPriority(rs.getString("priority"));

            bean.setPublished(rs.getBoolean("is_published"));

            Timestamp publishDate = rs.getTimestamp("publish_date");

            if(publishDate != null) {
                bean.setPublishDate(publishDate.toLocalDateTime());
            }


            Timestamp expiryDate = rs.getTimestamp("expiry_date");

            if(expiryDate != null) {
                bean.setExpiryDate(expiryDate.toLocalDateTime());
            }


            Timestamp createdAt = rs.getTimestamp("created_at");

            if(createdAt != null) {
                bean.setCreatedAt(createdAt.toLocalDateTime());
            }


            Timestamp updatedAt = rs.getTimestamp("updated_at");

            if(updatedAt != null) {
                bean.setUpdatedAt(updatedAt.toLocalDateTime());
            }

            bean.setExpiryDate(
                rs.getTimestamp("expiry_date").toLocalDateTime()
            );

            bean.setCreatedAt(
                rs.getTimestamp("created_at").toLocalDateTime()
            );

            bean.setUpdatedAt(
                rs.getTimestamp("updated_at").toLocalDateTime()
            );

            return bean;
        }, courseID);
    }

}