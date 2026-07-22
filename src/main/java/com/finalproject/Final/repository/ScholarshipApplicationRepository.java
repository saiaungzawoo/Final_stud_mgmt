package com.finalproject.Final.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.model.ScholarshipApplicationBean;
import com.finalproject.Final.model.ScholarshipBean;

@Repository
public class ScholarshipApplicationRepository {
	
	@Autowired
	private JdbcTemplate jdbc;
	
	
	 // Insert for admin
    public int insert(ScholarshipBean obj){

        String sql =
        """
        INSERT INTO scholarship
        (
        scholarshipID,
        courseID,
        name,
        description,
        discount_type,
        discount_value,
        max_recipients,
        application_deadline,
        is_active,
        createdByID,
        created_at,
        updated_at
        )
        VALUES(?,?,?,?,?,?,?,?,?,?,?,?)
        """;


        return jdbc.update(sql,

        obj.getScholarshipID(),
        obj.getCourseID(),
        obj.getScholarshipName(),
        obj.getDescription(),
        obj.getDiscountType(),
        obj.getDiscountValue(),
        obj.getMaxRecipients(),
        obj.getApplicationDeadline(),
        obj.getIsActive(),
        obj.getCreatedByUserID(),
        obj.getCreatedAt(),
        obj.getUpdatedAt()

        );

    }



	
	
	//for admin to update 
	public ScholarshipBean findByScholarshipId(String id) {

	    String sql = """
	            SELECT * FROM scholarship
	            WHERE scholarshipID = ?
	            """;

	    return jdbc.queryForObject(sql, (rs, rowNum) -> {

	        ScholarshipBean obj = new ScholarshipBean();

	        obj.setScholarshipID(rs.getString("scholarshipID"));
	        obj.setCourseID(rs.getString("courseID"));
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

	        obj.setIsActive(
	                rs.getInt("is_active"));

	        return obj;
 },
	    		id);
	}
	
	
	// Update for admin 

    public int update(ScholarshipBean obj){


        String sql=
        """
        UPDATE scholarship SET
        name=?,
        description=?,
        discount_type=?,
        discount_value=?,
        max_recipients=?,
        application_deadline=?,
        is_active=?,
        updated_at=?
        WHERE scholarshipID=?
        """;


        return jdbc.update(sql,

        obj.getScholarshipName(),
        obj.getDescription(),
        obj.getDiscountType(),
        obj.getDiscountValue(),
        obj.getMaxRecipients(),
        obj.getApplicationDeadline(),
        obj.getIsActive(),
        obj.getUpdatedAt(),
        obj.getScholarshipID()

        );

    }

//    // Delete for admin
//
//    public int delete(String id){
//
//        return jdbc.update(
//        "DELETE FROM scholarship WHERE scholarshipID=?",
//        id);
//
//    }

    
    // Select All for admin

    public List<ScholarshipBean> getAll() {

        String sql = "SELECT * FROM scholarship";

        return jdbc.query(sql, (rs, rowNum) -> {

            ScholarshipBean obj = new ScholarshipBean();

            obj.setScholarshipID(rs.getString("scholarshipID"));
            obj.setCourseID(rs.getString("courseID"));
            obj.setScholarshipName(rs.getString("name"));
            obj.setDescription(rs.getString("description"));
            obj.setDiscountType(rs.getString("discount_type"));
            obj.setDiscountValue(rs.getString("discount_value"));
            obj.setMaxRecipients(rs.getInt("max_recipients"));
            Date deadline = rs.getDate("application_deadline");

            if (deadline != null) {
                obj.setApplicationDeadline(
                        deadline.toLocalDate()
                );
            }
            obj.setIsActive(rs.getInt("is_active"));
            obj.setCreatedByUserID(rs.getString("createdByID"));

            if (rs.getTimestamp("created_at") != null) {
                obj.setCreatedAt(
                    rs.getTimestamp("created_at").toLocalDateTime()
                );
            }

            if (rs.getTimestamp("updated_at") != null) {
                obj.setUpdatedAt(
                    rs.getTimestamp("updated_at").toLocalDateTime()
                );
            }

            return obj;
        } );
    }


    // 1. Admin View All  Scholarship Applications
    // User + Scholarship Information
    public List<ScholarshipApplicationBean> getAllApplications(){


        String sql = """
  SELECT sa.scholarshipApplicationID,
        sa.application_date,
        sa.reason,
        sa.supporting_documents,
        sa.status,
        sa.created_at,
        sa.updated_at,
s.name AS scholarship_name,
 u.userID,
u.name AS user_name,
        u.email,
        u.phone_no,
        u.address
        FROM scholarship_application sa
         JOIN scholarship s
ON sa.scholarshipID = s.scholarshipID
 JOIN user u
ON sa.userID = u.userID
ORDER BY sa.created_at DESC
 """;

        return jdbc.query(sql, (rs,rowNum)->{
 ScholarshipApplicationBean obj =
                    new ScholarshipApplicationBean();
 obj.setScholarshipApplicationID(
                    rs.getString("scholarshipApplicationID")
            );
// Scholarship

            obj.setScholarshipName(
                    rs.getString("scholarship_name")
            );

            // User
 obj.setUserID(rs.getString("userID")
            );
obj.setUserName(rs.getString("user_name")
            );
 obj.setEmail(
  rs.getString("email")
            );
 obj.setPhoneNo(rs.getString("phone_no") 
		 );
obj.setAddress( rs.getString("address")
            );
            // Application
obj.setApplicationDate(
                    rs.getDate("application_date")
                    .toLocalDate()
            );
 obj.setReason(
                    rs.getString("reason")
            );
 obj.setSupportingDocuments(
                    rs.getString("supporting_documents")
            );
  obj.setStatus(
                    rs.getString("status")
            );
  obj.setCreatedAt(
                    rs.getTimestamp("created_at")
                    .toLocalDateTime()
            );
  if(rs.getTimestamp("updated_at") != null){

                obj.setUpdatedAt(
                    rs.getTimestamp("updated_at")
                    .toLocalDateTime()
                );
            }

            return obj;
       });


    }


    // 2. Admin View Application Detail
    
    public ScholarshipApplicationBean getApplicationDetail(String id){
 String sql = """
 SELECT
 sa.scholarshipID,
sa.scholarshipApplicationID,
sa.application_date,
sa.reason,
sa.supporting_documents,
 sa.status,
sa.review_notes,
s.name AS scholarship_name,
u.userID,
u.name AS user_name,
u.email,
u.phone_no,
u.address
FROM scholarship_application sa
JOIN scholarship s
ON sa.scholarshipID = s.scholarshipID
JOIN user u
ON sa.userID = u.userID
WHERE sa.scholarshipApplicationID = ?
 """;
 return jdbc.queryForObject(
  sql,
(rs,rowNum)->{
 ScholarshipApplicationBean obj =new ScholarshipApplicationBean();
 obj.setScholarshipID(rs.getString("scholarshipID"));
obj.setScholarshipApplicationID(rs.getString("scholarshipApplicationID")
                    );
 obj.setScholarshipName( rs.getString("scholarship_name")
                    );
 obj.setUserID(rs.getString("userID")
		 );
 obj.setUserName(rs.getString("user_name")
		 );             
 obj.setEmail(rs.getString("email")                     
		 );
 obj.setPhoneNo(rs.getString("phone_no")
		 );
 obj.setAddress(rs.getString("address")
		 );       
 obj.setReason(rs.getString("reason")                   
		 );            
 obj.setSupportingDocuments(rs.getString("supporting_documents")
		 );
 obj.setStatus(rs.getString("status")
		 );           
 obj.setReviewNotes(rs.getString("review_notes")
		 );                  
                   
  return obj;
 },
 id);

    }


    
    // 3. Admin Approve / Reject
 public int updateApplicationStatus(String applicationID,String status,
		 String adminID,String reviewNotes){
    String sql = """
UPDATE scholarship_application
SET status = ?,
reviewedByID = ?,
reviewed_at = NOW(),
review_notes = ?,
updated_at = NOW()
WHERE scholarshipApplicationID = ?
 """;



        return jdbc.update(
                sql,
                status,
                adminID,
                reviewNotes,
                applicationID
        );


    }
 
 

 
	
 
//Select Course Name for Scholarship Create
public List<CourseBean> getAllCourseNa(){

  String sql = """
      SELECT 
          courseID,
          name
      FROM course
      ORDER BY name
      """;


  return jdbc.query(sql, (rs, rowNum) -> {

      CourseBean course = new CourseBean();

      course.setCourseId(
              rs.getString("courseID")
      );

      course.setName(
              rs.getString("name")
      );

      return course;

  });
}



public List<ScholarshipApplicationBean> getApplicationsByScholarship(String scholarshipID) {

    String sql = """
        SELECT
            sa.scholarshipApplicationID,
            sa.application_date,
            sa.reason,
            sa.supporting_documents,
            sa.status,
sa.review_notes AS reviewNotes,
            u.userID,
            u.name AS user_name,
            u.email,
            u.phone_no,
            u.address,

            s.name AS scholarship_name,

            c.name AS course_name

        FROM scholarship_application sa

        JOIN user u
            ON sa.userID = u.userID

        JOIN scholarship s
            ON sa.scholarshipID = s.scholarshipID

        LEFT JOIN course c
            ON s.courseID = c.courseID

        WHERE sa.scholarshipID = ?

        ORDER BY sa.application_date DESC
        """;

    return jdbc.query(sql, (rs, rowNum) -> {

        ScholarshipApplicationBean app = new ScholarshipApplicationBean();

        app.setScholarshipApplicationID(
                rs.getString("scholarshipApplicationID"));

        app.setApplicationDate(
                rs.getDate("application_date").toLocalDate());

        app.setReason(
                rs.getString("reason"));

        app.setSupportingDocuments(
                rs.getString("supporting_documents"));

        app.setStatus(
                rs.getString("status"));
        
        app.setReviewNotes(
                rs.getString("reviewNotes"));

        // User
        app.setUserID(
                rs.getString("userID"));

        app.setUserName(
                rs.getString("user_name"));

        app.setEmail(
                rs.getString("email"));

        app.setPhoneNo(
                rs.getString("phone_no"));

        app.setAddress(
                rs.getString("address"));

        // Scholarship
        app.setScholarshipName(
                rs.getString("scholarship_name"));

        // Course
        app.setCourseName(
                rs.getString("course_name"));

        return app;

    }, scholarshipID);
}

//4. Count approved applications for a specific scholarship
public int getApprovedCountByScholarship(String scholarshipID) {
    String sql = """
        SELECT COUNT(*) 
        FROM scholarship_application 
        WHERE scholarshipID = ? AND status = 'Approved'
        """;
    
    // queryForObject returns the count as an Integer
    return jdbc.queryForObject(sql, Integer.class, scholarshipID);
}

// 5. Update scholarship active status to inactive (0) when max limit is reached
public int updateScholarshipStatus(String scholarshipID, String status) {
    String sql = """
        UPDATE scholarship 
        SET is_active = 0,
            updated_at = NOW()
        WHERE scholarshipID = ?
        """;
    
    return jdbc.update(sql, scholarshipID);
}

//6 scholarship inactive
public void makeInactive(String scholarshipID) {
	String sql="Update scholarship set status='Inactive' where scholarshipID=?";
	
	
	jdbc.update(sql,scholarshipID);
}

//7reject all pending
public void rejectPendingApplications(String scholarshipID,
        String adminID,
        String reviewNotes) {

    String sql = """
        UPDATE scholarship_application
        SET
            status = ?,
            reviewedByID = ?,
            reviewed_at = NOW(),
            review_notes = ?,
            updated_at = NOW()
        WHERE scholarshipID = ?
          AND status = ?
        """;

    jdbc.update(
            sql,
            "Rejected",
            adminID,
            reviewNotes,
            scholarshipID,
            "Pending"
    );
}

}
  


