package com.finalproject.Final.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.AnnouncementRecipientBean;

@Repository
public class AnnouncementRecipientRepository {

    private final JdbcTemplate jdbcTemplate;

    public AnnouncementRecipientRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    // Save Recipient
    public void saveRecipient(AnnouncementRecipientBean bean) {

        String sql = """
                INSERT INTO announcement_recipient
                (
                    announcementRecipientID,
                    announcementID,
                    userID,
                    is_read,
                    is_acknowledged,
                    is_deleted
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;


        jdbcTemplate.update(sql,

                UUID.randomUUID().toString(),

                bean.getAnnouncementID(),

                bean.getUserID(),

                false,

                false,

                false
        );
    }


    // Mark as Read
    public void updateReadStatus(String announcementID, String userID) {

        String sql = """
                UPDATE announcement_recipient
                SET
                    is_read = 1,
                    read_at = NOW()
                WHERE announcementID = ?
                AND userID = ?
                """;


        jdbcTemplate.update(sql,
                announcementID,
                userID
        );
    }


    // Mark Acknowledge
    public void updateAcknowledge(String announcementID, String userID) {

        String sql = """
                UPDATE announcement_recipient
                SET
                    is_acknowledged = 1,
                    acknowledged_at = NOW()
                WHERE announcementID = ?
                AND userID = ?
                """;


        jdbcTemplate.update(sql,
                announcementID,
                userID
        );
    }
 // Get all student IDs
    public List<String> getStudentIDs(){

        String sql = """
                SELECT u.userID
                FROM user u
                JOIN role r
                ON u.roleID = r.roleID
                WHERE r.name = 'Student'
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getString("userID")
        );
    }


    // Get all teacher IDs
    public List<String> getTeacherIDs(){

        String sql = """
                SELECT u.userID
                FROM user u
                JOIN role r
                ON u.roleID = r.roleID
                WHERE r.name = 'Teacher'
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getString("userID")
        );
    }


    // Get all active user IDs
    public List<String> getAllUserIDs(){

        String sql = """
                SELECT userID
                FROM user
                WHERE is_active = 1
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getString("userID")
        );
    }
    public void deleteByAnnouncementID(String announcementID) {

        String sql = """
                DELETE FROM announcement_recipient
                WHERE announcementID = ?
                """;

        jdbcTemplate.update(sql, announcementID);
    }
    public List<AnnouncementRecipientBean> getRecipientStatus(String announcementID){

        String sql = """
            SELECT 
                ar.userID,
                u.name,
                ar.is_read,
                ar.is_acknowledged
            FROM announcement_recipient ar
            JOIN user u
            ON ar.userID = u.userID
            WHERE ar.announcementID = ?
            """;
 return jdbcTemplate.query(sql, (rs, rowNum) -> {

            AnnouncementRecipientBean bean =
                    new AnnouncementRecipientBean();


            bean.setUserID(
                    rs.getString("userID")
            );


            bean.setUserName(
                    rs.getString("name")
            );


            bean.setReadStatus(
                    rs.getBoolean("is_read")
            );


            bean.setAcknowledged(
                    rs.getBoolean("is_acknowledged")
            );


            return bean;

        }, announcementID);
    }
}
