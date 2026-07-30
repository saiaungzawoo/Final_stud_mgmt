package com.finalproject.Final.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.model.EnrollmentBean;
import com.finalproject.Final.model.EnrollmentStatisticsBean;

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

	//dont delete
	// FIND ENROLLMENTS BY USER
//	public List<EnrollmentBean> findByUser(String userId) {
//
//		String sql = "SELECT e.*, " + "c.name AS course_title " + "FROM enrollment e "
//				+ "JOIN course c ON e.courseID = c.courseID " + "WHERE e.userID = ?";
//
//		return jdbc.query(sql, new EnrollmentRowMapper(), userId);
//	}
//	public List<EnrollmentBean> findByUser(String userId) {
//
//	    String sql = """
//	        SELECT e.*, c.name AS course_title, u.name AS teacher_name, COALESCE(SUM(ip.paid_amount),0) AS total_paid,  e.final_fee - COALESCE(SUM(ip.paid_amount),0)
//	                AS remaining_balance, COUNT(DISTINCT iritem.installmentRuleItemID)
//	                AS total_installments, 
//                    COUNT(DISTINCT CASE
//    WHEN ip.status = 'Paid'
//    THEN ip.installmentPlanID
//END) AS completed_installments
//                FROM enrollment e 
//                JOIN course c
//	        ON e.courseID = c.courseID
//
//	        LEFT JOIN user u
//	        ON c.teacherID = u.userID
//
//	        LEFT JOIN installment_plan ip
//	        ON e.enrollmentID = ip.enrollmentID
//
//	        LEFT JOIN installment_rule_item iritem
//	        ON ip.installmentRuleItemID =
//	           iritem.installmentRuleItemID
//
//	        WHERE e.userID=?
//
//	        GROUP BY e.enrollmentID
//
//	        ORDER BY e.created_at DESC
//	        """;
//
//	    return jdbc.query(
//	            sql,
//	            new EnrollmentRowMapper(),
//	            userId);
//	}
	
//	public List<EnrollmentBean> findByUser(String userId) {
//
//		String sql = """
//
//		SELECT 
//
//		e.*,
//
//		c.name AS course_title,
//
//		u.name AS teacher_name,
//
//
//		COALESCE(SUM(DISTINCT ip.paid_amount),0)
//		AS total_paid,
//
//
//		e.final_fee -
//		(
//		 COALESCE(SUM(DISTINCT ip.paid_amount),0)
//		 +
//		 COALESCE(SUM(DISTINCT p.amount),0)
//		)
//		AS remaining_balance,
//
//
//		COUNT(DISTINCT iri.installmentRuleItemID)
//		AS total_installments,
//
//
//		COUNT(DISTINCT CASE
//		 WHEN ip.status='Paid'
//		 THEN ip.installmentPlanID
//		END)
//		AS completed_installments
//
//
//		FROM enrollment e
//
//
//		JOIN course c
//		ON e.courseID=c.courseID
//
//
//		LEFT JOIN user u
//		ON c.teacherID=u.userID
//
//
//		LEFT JOIN installment_rule_item iri
//		ON e.installmentRuleID=
//		iri.installmentRuleID
//
//
//		LEFT JOIN installment_plan ip
//		ON e.enrollmentID=
//		ip.enrollmentID
//
//
//		LEFT JOIN payment p
//		ON e.enrollmentID=
//		p.enrollmentID
//
//
//		WHERE e.userID=?
//
//
//		GROUP BY e.enrollmentID
//
//
//		ORDER BY e.created_at DESC
//
//		""";
//
//
//		return jdbc.query(
//		        sql,
//		        new EnrollmentRowMapper(),
//		        userId
//		);
//
//		}
	
	public List<EnrollmentBean> findByUser(String userId) {

	    String sql = """
	        SELECT
	            e.*,
	            c.name AS course_title,
	            u.name AS teacher_name
	        FROM enrollment e

	        JOIN course c
	            ON e.courseID = c.courseID

	        LEFT JOIN user u
	            ON c.teacherID = u.userID

	        WHERE e.userID = ?

	        ORDER BY e.created_at DESC
	        """;

	    return jdbc.query(
	            sql,
	            new EnrollmentRowMapper(),
	            userId);

	}

	// UPDATE STATUS
	public void updateStatus(String enrollmentId, String status,  String reason) {

		String sql = """
				UPDATE enrollment
				SET status=?,
				    status_reason=?,
				    updated_at=NOW()
				WHERE enrollmentID=?
				""";

		jdbc.update(sql, status, reason, enrollmentId);
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
	
	//search enrolled courses
	public List<CourseBean> searchMyCourses(
	        String userId,
	        String keyword,
	        String categoryId
	) {

	    String sql = """
	        SELECT 
	            c.*,
	            cat.name AS category_name,
	            sub.name AS subcategory_name,
	            u.name AS teacher_name

	        FROM enrollment e

	        JOIN course c
	            ON e.courseID = c.courseID

	        LEFT JOIN course_category cat
	            ON c.courseCategoryID = cat.courseCategoryID

	        LEFT JOIN subcategory sub
	            ON c.subcategoryID = sub.subcategoryID

	        LEFT JOIN user u
	            ON c.teacherID = u.userID

	        WHERE e.userID = ?
	        AND e.status = 'Active'
	        """;


	   
		List<Object> params = new ArrayList<>();

	    params.add(userId);


	    if(keyword != null && !keyword.isBlank()) {

	        sql += """
	            AND c.name LIKE ?
	            """;

	        params.add("%" + keyword + "%");
	    }


	    if(categoryId != null && !categoryId.isBlank()) {

	        sql += """
	            AND c.courseCategoryID = ?
	            """;

	        params.add(categoryId);
	    }


	    sql += """
	        ORDER BY c.created_at DESC
	        """;


	    return jdbc.query(
	            sql,
	            new CourseRowMapper(),
	            params.toArray()
	    );

	}
	
	
	public int countEnrolledStudents(String courseId) {

	    String sql = """
	        SELECT COUNT(*)
	        FROM enrollment
	        WHERE courseID = ?
	        AND status IN ('Pending', 'Active', 'Completed')
	        """;

	    return jdbc.queryForObject(
	            sql,
	            Integer.class,
	            courseId
	    );
	}
	
	//admin enroll list
//	public List<EnrollmentBean> getAllEnrollments(){
//
//
//	    String sql = """
//
//	        SELECT
//
//	            e.*,
//
//	            u.name AS user_name,
//
//	            c.name AS course_title,
//
//
//	            pt.name AS payment_type_name,
//
//
//	            COALESCE(
//	                SUM(DISTINCT p.amount),
//	                0
//	            )
//	            AS total_paid,
//
//
//	            (
//	                e.final_fee -
//	                COALESCE(
//	                    SUM(DISTINCT p.amount),
//	                    0
//	                )
//	            )
//	            AS remaining_balance,
//
//
//	            COUNT(
//	                DISTINCT ip.installmentPlanID
//	            )
//	            AS total_installments,
//
//
//	            COUNT(
//	                DISTINCT CASE
//	                    WHEN ip.status='Paid'
//	                    THEN ip.installmentPlanID
//	                END
//	            )
//	            AS completed_installments
//
//
//
//	        FROM enrollment e
//
//
//
//	        JOIN user u
//
//	            ON e.userID = u.userID
//
//
//
//	        JOIN course c
//
//	            ON e.courseID = c.courseID
//
//
//
//	        LEFT JOIN payment_type pt
//
//	            ON e.paymentTypeID = pt.paymentTypeID
//
//
//
//	        LEFT JOIN payment p
//
//	            ON e.enrollmentID = p.enrollmentID
//
//	            AND p.status='Success'
//
//
//
//	        LEFT JOIN installment_plan ip
//
//	            ON e.enrollmentID = ip.enrollmentID
//
//
//
//	        GROUP BY e.enrollmentID
//
//
//
//	        ORDER BY e.created_at DESC
//
//	        """;
//
//
//
//	    return jdbc.query(
//	            sql,
//	            new AdminEnrollmentRowMapper()
//	    );
//
//	}

	public List<EnrollmentBean> getAllEnrollments(){


	    String sql = """

	        SELECT

	            e.*,

	            u.name AS user_name,
	            u.email AS user_email,

	            c.name AS course_title,


	            pt.name AS payment_type_name,


	            COALESCE(pay.total_paid,0)
	            AS total_paid,


	            (
	                e.final_fee -
	                COALESCE(pay.total_paid,0)
	            )
	            AS remaining_balance,


	            COALESCE(ins.total_installments,0)
	            AS total_installments,


	            COALESCE(ins.completed_installments,0)
	            AS completed_installments



	        FROM enrollment e



	        JOIN user u

	            ON e.userID = u.userID



	        JOIN course c

	            ON e.courseID = c.courseID



	        LEFT JOIN payment_type pt

	            ON e.paymentTypeID = pt.paymentTypeID




	        /*
	         PAYMENT SUMMARY
	         */

	        LEFT JOIN
	        (

	            SELECT

	                enrollmentID,

	                SUM(amount) AS total_paid


	            FROM payment


	            WHERE status='Success'


	            GROUP BY enrollmentID


	        ) pay


	        ON e.enrollmentID = pay.enrollmentID




	        /*
	         INSTALLMENT SUMMARY
	         */

	        LEFT JOIN
	        (

	            SELECT

	                enrollmentID,


	                COUNT(installmentPlanID)
	                AS total_installments,


	                COUNT(
	                    CASE
	                        WHEN status='Paid'
	                        THEN installmentPlanID
	                    END
	                )
	                AS completed_installments



	            FROM installment_plan


	            GROUP BY enrollmentID


	        ) ins


	        ON e.enrollmentID = ins.enrollmentID




	        ORDER BY e.created_at DESC


	        """;



	    return jdbc.query(
	            sql,
	            new AdminEnrollmentRowMapper()
	    );

	}
	
	public EnrollmentStatisticsBean getEnrollmentStatistics() {

	    EnrollmentStatisticsBean stats =
	            new EnrollmentStatisticsBean();

	    /*
	     Total enrollments
	     */
	    String totalSql = """
	            SELECT COUNT(*)
	            FROM enrollment
	            """;

	    stats.setTotalEnrollments(

	            jdbc.queryForObject(
	                    totalSql,
	                    Integer.class
	            )

	    );



	    /*
	     Active students
	     */
	    String activeSql = """
	            SELECT COUNT(*)
	            FROM enrollment
	            WHERE status='Active'
	            """;

	    stats.setActiveStudents(

	            jdbc.queryForObject(
	                    activeSql,
	                    Integer.class
	            )

	    );



	    /*
	     Completed
	     */
	    String completedSql = """
	            SELECT COUNT(*)
	            FROM enrollment
	            WHERE status='Completed'
	            """;

	    stats.setCompletedCourses(

	            jdbc.queryForObject(
	                    completedSql,
	                    Integer.class
	            )

	    );



	    /*
	     Dropped
	     */
	    String droppedSql = """
	            SELECT COUNT(*)
	            FROM enrollment
	            WHERE status='Dropped'
	            """;

	    stats.setDroppedStudents(

	            jdbc.queryForObject(
	                    droppedSql,
	                    Integer.class
	            )

	    );

	    return stats;

	}
	
	
}