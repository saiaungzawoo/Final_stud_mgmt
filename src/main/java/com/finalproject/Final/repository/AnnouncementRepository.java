package com.finalproject.Final.repository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.AnnouncementBean;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
@Repository
public class AnnouncementRepository {
	 private final JdbcTemplate jdbcTemplate;

	    public AnnouncementRepository(JdbcTemplate jdbcTemplate) {
	        this.jdbcTemplate = jdbcTemplate;
	    }

	    private final RowMapper<AnnouncementBean> rowMapper = (rs, rowNum) -> {
	        AnnouncementBean announcement = new AnnouncementBean();

	        announcement.setAnnouncementID(rs.getString("announcementID"));
	        announcement.setCreatedByID( rs.getString("createdByID"));
	        announcement.setCourseID(rs.getString("courseID"));
	        announcement.setTitle(rs.getString("title"));
	        announcement.setContent(rs.getString("content"));
	        announcement.setTargetType(rs.getString("target_type"));
	        announcement.setPriority(rs.getString("priority"));
	        announcement.setPublished(rs.getBoolean("is_published"));

	        Timestamp publishDate = rs.getTimestamp("publish_date");
	        if (publishDate != null) {
	            announcement.setPublishDate(publishDate.toLocalDateTime());
	        }

	        Timestamp expiryDate = rs.getTimestamp("expiry_date");
	        if (expiryDate != null) {
	            announcement.setExpiryDate(expiryDate.toLocalDateTime());
	        }

	        Timestamp updatedAt = rs.getTimestamp("updated_at");
	        if (updatedAt != null) {
	            announcement.setUpdatedAt(updatedAt.toLocalDateTime());
	        }

	        return announcement;
	    };

	    public List<AnnouncementBean> findByTeacherId(String teacherId) {

	        String sql = """
	                SELECT *
	                FROM announcement
	                WHERE
	                    createdByID = ?
	                    OR (
	                        target_type IN ('ALL','ALL_TEACHERS')
	                        AND is_published = 1
	                    )
	                ORDER BY publish_date DESC, announcementID DESC
	                """;

	        return jdbcTemplate.query(sql, rowMapper, teacherId);
	    }

	    public AnnouncementBean findByIdAndTeacherId ( String announcementID, String teacherID) {
	        String sql = """
	                SELECT * FROM announcement WHERE announcementID = ? AND createdByID = ? """;

	        return jdbcTemplate.queryForObject(sql, rowMapper, announcementID,teacherID);
	    }

	    public int save(AnnouncementBean announcement) {
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

    VALUES
    (?,?,?,?,?,?,?,?,?,?)

    """;


	        return jdbcTemplate.update(
	                sql,
	                UUID.randomUUID().toString(),
	                announcement.getCreatedByID(),
	                announcement.getCourseID(),
	                announcement.getTitle(),
	                announcement.getContent(),
	                announcement.getTargetType(),
	                announcement.getPriority(),
	                announcement.isPublished() ? 1 : 0,
	                announcement.getPublishDate(),
	                announcement.getExpiryDate()
	        );
	    }

	    public int deleteByIdAndTeacherId(String announcementID, String teacherID) {

	        String sql = """
	                DELETE FROM announcement
	                WHERE announcementID = ?
	                AND createdByID = ?
	                """;

	        return jdbcTemplate.update(sql, announcementID, teacherID);
	    }
	    public int update(AnnouncementBean announcement) {

	    	 String sql = """
	    	            UPDATE announcement
	    	            SET
	    	                title = ?,
	    	                content = ?,
	    	                target_type = ?,
	    	                priority = ?,
	    	                is_published = ?,
	    	                publish_date = ?,
	    	                expiry_date = ?
	    	            WHERE announcementID = ?
	    	            """;




	    	 return jdbcTemplate.update(
	    			    sql,
	    			    announcement.getTitle(),
	    			    announcement.getContent(),
	    			    announcement.getTargetType(),
	    			    announcement.getPriority(),
	    			    announcement.isPublished() ? 1 : 0,
	    			    announcement.getPublishDate(),
	    			    announcement.getExpiryDate(),
	    			    announcement.getAnnouncementID()
	    			);

	    }
	    public AnnouncementBean getAnnouncementById(String id) {

	        String sql = """
	                SELECT 
	                    announcementID,
	                    title,
	                    content
	                FROM announcement
	                WHERE announcementID = ?
	                """;

	        return jdbcTemplate.queryForObject(
	                sql,
	                (rs, rowNum) -> {

	                    AnnouncementBean announcement = new AnnouncementBean();

	                    announcement.setAnnouncementID(
	                        rs.getString("announcementID")
	                    );

	                    announcement.setTitle(
	                        rs.getString("title")
	                    );

	                    announcement.setContent(
	                        rs.getString("content")
	                    );

	                    return announcement;
	                },
	                id
	        );
	    }
}
