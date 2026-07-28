package com.finalproject.Final.repository;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.ScholarshipApplicationBean;
import com.finalproject.Final.model.ScholarshipBean;



@Repository
public class ScholarshipsRepository {

	@Autowired
	private JdbcTemplate jdbc;
	
	 // for user view 
    public List<ScholarshipBean> getActiveScholarships(){

    	String sql =  "SELECT s.scholarshipID,s.courseID,\r\n"
    			+ "c.name,s.name,s.description,s.discount_type,s.discount_value,s.max_recipients,s.application_deadline\r\n"
    			+ "FROM scholarship s JOIN course c\r\n"
    			+ "ON s.courseID= c.courseID WHERE s.is_active = 1\r\n"
    			+ "ORDER BY s.created_at DESC";
    	
    	return jdbc.query(sql,(rs,rowNum)->{
ScholarshipBean obj = new ScholarshipBean();

obj.setScholarshipID(rs.getString("scholarshipID"));
obj.setCourseID(rs.getString("courseID"));
obj.setCourseName(rs.getString("name"));
obj.setScholarshipName(rs.getString("name"));
obj.setDescription(rs.getString("description"));
obj.setDiscountType(rs.getString("discount_type"));
obj.setDiscountValue(rs.getString("discount_value"));
obj.setMaxRecipients(rs.getInt("max_recipients"));
Date deadline = rs.getDate("application_deadline");

if(deadline != null){
    obj.setApplicationDeadline(
        deadline.toLocalDate()
    );
}
       return obj;
        });

    	}


//View one scholarship
 // View one scholarship for user
    public ScholarshipBean findById(String id) {

        String sql = """
            SELECT
                s.scholarshipID,
                s.courseID,
                c.name ,
                s.name ,
                s.description,
                s.discount_type,
                s.discount_value,
                s.max_recipients,
                s.application_deadline,
                s.is_active
            FROM scholarship s
            JOIN course c
                ON s.courseID = c.courseID
            WHERE s.scholarshipID = ?
            """;

        return jdbc.queryForObject(sql, new Object[] { id }, (rs, rowNum) -> {

            ScholarshipBean obj = new ScholarshipBean();

            obj.setScholarshipID(rs.getString("scholarshipID"));
            obj.setCourseID(rs.getString("courseID"));
            obj.setCourseName(rs.getString("name"));
            obj.setScholarshipName(rs.getString("name"));
            obj.setDescription(rs.getString("description"));
            obj.setDiscountType(rs.getString("discount_type"));
            obj.setDiscountValue(rs.getString("discount_value"));
            obj.setMaxRecipients(rs.getInt("max_recipients"));
            Date deadline = rs.getDate("application_deadline");

            if(deadline != null){
                obj.setApplicationDeadline(
                    deadline.toLocalDate()
                );
            }
            obj.setIsActive(rs.getInt("is_active"));

            return obj;
        });

    }
    
    
    //insert form for student
    public int insert(ScholarshipApplicationBean obj){


        String sql = "INSERT INTO scholarship_application(scholarshipApplicationID,"
        		+ "scholarshipID, userID,application_date,reason,"
        		+ "supporting_documents,status, created_at,updated_at)"
        		+ "VALUES(?,?,?,?,?,?,?,NOW(),"
        		+ "NOW())";

                


        return jdbc.update(sql,

                UUID.randomUUID().toString(),

                obj.getScholarshipID(),

                obj.getUserID(),

                obj.getApplicationDate(),

                obj.getReason(),

                obj.getSupportingDocuments(),
"Pending"
                );
             }
    
    
    //for user 
    public List<ScholarshipApplicationBean> getMyApplications(String userID){

        String sql = """
            SELECT
                sa.scholarshipApplicationID,
                s.name AS scholarshipName,
                sa.application_date,
                sa.reason,
                sa.supporting_documents,
                sa.status
            FROM scholarship_application sa
            JOIN scholarship s
            ON sa.scholarshipID = s.scholarshipID
            WHERE sa.userID = ?
            ORDER BY sa.application_date DESC
            """;

        return jdbc.query(sql,new Object[]{userID},(rs,rowNum)->{

            ScholarshipApplicationBean obj =
                    new ScholarshipApplicationBean();

            obj.setScholarshipApplicationID(
                    rs.getString("scholarshipApplicationID"));

            obj.setScholarshipName(
                    rs.getString("scholarshipName"));

            obj.setApplicationDate(
                    rs.getDate("application_date").toLocalDate());

            obj.setReason(
                    rs.getString("reason"));

            obj.setSupportingDocuments(
                    rs.getString("supporting_documents"));

            obj.setStatus(
                    rs.getString("status"));

            return obj;

        });

    }
    
    //apply one 
    public boolean hasApplied(String scholarshipID, String userID) {

        String sql = """
            SELECT COUNT(*)
            FROM scholarship_application
            WHERE scholarshipID = ?
            AND userID = ?
            """;

        Integer count = jdbc.queryForObject(
                sql,
                Integer.class,
                scholarshipID,
                userID
        );

        return count != null && count > 0;
    }
}

       
                
                

        

 






