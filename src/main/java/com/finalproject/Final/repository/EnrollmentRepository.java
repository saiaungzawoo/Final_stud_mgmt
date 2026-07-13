package com.finalproject.Final.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.model.EnrollmentBean;

@Repository
public class EnrollmentRepository {

	@Autowired
	private JdbcTemplate jdbc;

	// CREATE ENROLLMENT
	public String save(
	        String userId,
	        String courseId,
	        LocalDate date,
	        Double originalFee,
	        Double finalFee
	) {

	    String enrollmentId = UUID.randomUUID().toString();

	    String sql =
	        "INSERT INTO enrollment "
	        +
	        "(enrollmentID,userID,courseID,"
	        +
	        "paymentTypeID,installmentRuleID,scholarshipApplicationID,"
	        +
	        "enrollment_date,"
	        +
	        "original_fee,"
	        +
	        "discount_amount,"
	        +
	        "final_fee,"
	        +
	        "payment_status,"
	        +
	        "status,"
	        +
	        "created_at,"
	        +
	        "updated_at)"
	        +
	        " VALUES "
	        +
	        "(?,?,?,NULL,NULL,NULL,?,?,?,?,"
	        +
	        "'Unpaid',"
	        +
	        "'Pending',"
	        +
	        "NOW(),NOW())";


	    jdbc.update(
	        sql,
	        enrollmentId,
	        userId,
	        courseId,
	        date,
	        originalFee,
	        0.0,
	        finalFee
	    );


	    return enrollmentId;
	}
	
	public void updateInstallmentRule(
	        String enrollmentId,
	        String installmentRuleId
	){

	    String sql =
	            """
	            UPDATE enrollment
	            SET installmentRuleID = ?,
	                updated_at = NOW()
	            WHERE enrollmentID = ?
	            """;

	    jdbc.update(
	            sql,
	            installmentRuleId,
	            enrollmentId
	    );

	}
	
	public void updatePartialPaymentStatus(
	        String enrollmentId
	){

	    String sql = """
	        UPDATE enrollment
	        SET
	            payment_status='Partial',
	            status='Active',
	            updated_at=NOW()
	        WHERE enrollmentID=?
	        """;

	    jdbc.update(sql, enrollmentId);

	}
	
	
	
	public EnrollmentBean findByUserAndCourse(String userId, String courseId) {

	    String sql = "SELECT *\r\n"
	    		+ "	        FROM enrollment\r\n"
	    		+ "	        WHERE userID = ?\r\n"
	    		+ "	        AND courseID = ?";

	    List<EnrollmentBean> list =
	            jdbc.query(sql, new EnrollmentRowMapper(), userId, courseId);

	    return list.isEmpty() ? null : list.get(0);
	}
	
	public void updatePaymentType(
	        String enrollmentId,
	        String paymentTypeId
	) {

	    String sql =
	            "UPDATE enrollment "
	          + "SET paymentTypeID=?, "
	          + "updated_at=NOW() "
	          + "WHERE enrollmentID=?";


	    jdbc.update(
	            sql,
	            paymentTypeId,
	            enrollmentId
	    );
	}
	
	public void updatePaymentStatus(
			String enrollmentId
			){

			String sql ="UPDATE enrollment\r\n"
					+ "			SET \r\n"
					+ "			payment_status='Fully Paid',\r\n"
					+ "			status='Active',\r\n"
					+ "			updated_at=NOW()\r\n"
					+ "			WHERE enrollmentID=?";
			


			jdbc.update(sql,enrollmentId);

			}

	// FIND BY ID
	public EnrollmentBean findById(String enrollmentId) {

		String sql = "SELECT * FROM enrollment WHERE enrollmentID = ?";

		return jdbc.queryForObject(sql, new EnrollmentRowMapper(), enrollmentId);
	}

	// FIND ENROLLMENTS BY USER
	public List<EnrollmentBean> findByUser(String userId) {

		String sql = "SELECT e.*, " + "c.name AS course_title " + "FROM enrollment e "
				+ "JOIN course c ON e.courseID = c.courseID " + "WHERE e.userID = ?";

		return jdbc.query(sql, new EnrollmentRowMapper(), userId);
	}

	// UPDATE STATUS
	public void updateStatus(String enrollmentId, String status) {

		String sql = "UPDATE enrollment " + "SET status=?, updated_at=NOW() " + "WHERE enrollmentID=?";

		jdbc.update(sql, status, enrollmentId);
	}

	// CHECK DUPLICATE ENROLLMENT
	public boolean existsByUserIdAndCourseId(String userId, String courseId) {

		String sql = "SELECT COUNT(*) " + "FROM enrollment " + "WHERE userID=? AND courseID=?";

		Integer count = jdbc.queryForObject(sql, Integer.class, userId, courseId);

		return count != null && count > 0;
	}

	// GET COURSES USER ENROLLED IN
	public List<CourseBean> getEnrolledCourses(String userId) {

		String sql = "SELECT " + "c.*, " + "cat.name AS category_name, " + "sub.name AS subcategory_name, "
				+ "u.name AS teacher_name " +

				"FROM enrollment e " +

				"JOIN course c " + "ON e.courseID = c.courseID " +

				"LEFT JOIN course_category cat " + "ON c.courseCategoryID = cat.courseCategoryID " +

				"LEFT JOIN subcategory sub " + "ON c.subcategoryID = sub.subcategoryID " +

				"LEFT JOIN `user` u " + "ON c.teacherID = u.userID " +

				"WHERE e.userID = ? " + "AND e.status = 'Active'";

		return jdbc.query(sql, new CourseRowMapper(), userId);
	}

}