package com.finalproject.Final.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.PaymentBean;
import com.finalproject.Final.model.PaymentHistoryBean;

@Repository
public class PaymentRepository {

	@Autowired
	private JdbcTemplate jdbc;

	// Save Payment
	// for full payment
	public String savePayment(String enrollmentId, String paymentMethodId, Double amount) {

		String paymentId = UUID.randomUUID().toString();

		String reference = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

		String sql = "INSERT INTO payment\r\n" + "    (\r\n" + "    paymentID,\r\n" + "    enrollmentID,\r\n"
				+ "    paymentMethodID,\r\n" + "    amount,\r\n" + "    payment_date,\r\n"
				+ "    transaction_reference,\r\n" + "    status,\r\n" + "    created_at,\r\n" + "    updated_at\r\n"
				+ "    )\r\n" + "    VALUES\r\n" + "    (?,?,?,?,CURRENT_DATE,?,'Success',NOW(),NOW())";

		jdbc.update(sql, paymentId, enrollmentId, paymentMethodId, amount, reference);

		return paymentId;

	}

	// for installment payment
	public String savePayment(String enrollmentId, String installmentPlanId, String paymentMethodId, Double amount) {

		String paymentId = UUID.randomUUID().toString();

		String reference = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

		String sql = """
				INSERT INTO payment
				(
				    paymentID,
				    enrollmentID,
				    installmentPlanID,
				    paymentMethodID,
				    amount,
				    payment_date,
				    transaction_reference,
				    status,
				    created_at,
				    updated_at
				)
				VALUES
				(
				    ?,?,?,?,?,CURRENT_DATE,
				    ?,
				    'Success',
				    NOW(),
				    NOW()
				)
				""";

		jdbc.update(sql, paymentId, enrollmentId, installmentPlanId, paymentMethodId, amount, reference);

		return paymentId;

	}

	// Find latest payment by enrollment
	public PaymentBean getByEnrollmentId(String enrollmentID) {

		String sql = "SELECT\r\n" + "            p.*,\r\n" + "            pm.name AS paymentMethodName,\r\n"
				+ "            pt.name AS paymentTypeName,\r\n" + "            u.name AS studentName\r\n"
				+ "        FROM payment p\r\n" + "       LEFT JOIN payment_method pm\r\n"
				+ "            ON p.paymentMethodID = pm.paymentMethodID\r\n" + "       LEFT JOIN enrollment e\r\n"
				+ "            ON p.enrollmentID = e.enrollmentID\r\n" + "       LEFT JOIN payment_type pt\r\n"
				+ "            ON e.paymentTypeID = pt.paymentTypeID\r\n" + "       LEFT JOIN user u\r\n"
				+ "            ON e.userID = u.userID\r\n" + "        WHERE p.enrollmentID = ?\r\n"
				+ "        ORDER BY p.created_at DESC\r\n" + "        LIMIT 1";

		List<PaymentBean> list = jdbc.query(sql, new PaymentRowMapper(), enrollmentID);

		return list.isEmpty() ? null : list.get(0);
	}

	// Find payment by UUID
//	public PaymentBean getById(String paymentID) {
//
//		String sql = """
//				    SELECT
//				    p.*,
//
//				    pm.name AS paymentMethodName,
//				    pt.name AS paymentTypeName,
//				    u.name AS studentName,
//
//				    ip.installment_number,
//				    ip.amount_due,
//				    ip.paid_amount,
//				    ip.due_date,
//
//				    ir.installment_count
//
//				FROM payment p
//
//				LEFT JOIN payment_method pm
//				ON p.paymentMethodID = pm.paymentMethodID
//
//				LEFT JOIN enrollment e
//				ON p.enrollmentID = e.enrollmentID
//
//				LEFT JOIN payment_type pt
//				ON e.paymentTypeID = pt.paymentTypeID
//
//				LEFT JOIN user u
//				ON e.userID = u.userID
//
//				LEFT JOIN installment_plan ip
//				ON p.installmentPlanID = ip.installmentPlanID
//
//				LEFT JOIN installment_rule_item iri
//				ON ip.installmentRuleItemID = iri.installmentRuleItemID
//
//				LEFT JOIN installment_rule ir
//				ON iri.installmentRuleID = ir.installmentRuleID
//
//				WHERE p.paymentID = ?
//
//				        		""";
//
//		List<PaymentBean> list = jdbc.query(sql, new PaymentRowMapper(), paymentID);
//
//		return list.isEmpty() ? null : list.get(0);
//	}

	// new fixed
	public PaymentBean getById(String paymentID) {

		String sql = """
					        SELECT
					            p.*,

					            pm.name AS paymentMethodName,
					            pt.name AS paymentTypeName,
					            u.name AS studentName,
					            c.name AS courseName,
					            c.fee AS courseFee,

					            (
				    SELECT COALESCE(SUM(pay.amount),0)
				    FROM payment pay
				    WHERE pay.enrollmentID = p.enrollmentID
				    AND pay.status = 'Success'
				) AS totalPaidAmount,

					            ip.installment_number,
					            ip.amount_due,
					            ip.paid_amount,
					            ip.due_date,

					            ir.installment_count

					        FROM payment p

					        LEFT JOIN payment_method pm
					            ON p.paymentMethodID = pm.paymentMethodID

					        LEFT JOIN enrollment e
					            ON p.enrollmentID = e.enrollmentID

					        LEFT JOIN course c
					            ON e.courseID = c.courseID

					        LEFT JOIN payment_type pt
					            ON e.paymentTypeID = pt.paymentTypeID

					        LEFT JOIN user u
					            ON e.userID = u.userID

					        LEFT JOIN installment_plan ip
					            ON p.installmentPlanID = ip.installmentPlanID

					        LEFT JOIN installment_rule_item iri
					            ON ip.installmentRuleItemID = iri.installmentRuleItemID

					        LEFT JOIN installment_rule ir
					            ON iri.installmentRuleID = ir.installmentRuleID

					        WHERE p.paymentID = ?
					        """;

		List<PaymentBean> list = jdbc.query(sql, new PaymentRowMapper(), paymentID);

		return list.isEmpty() ? null : list.get(0);
	}

	// Admin - get all payments
	// Admin payment list
	public List<PaymentBean> getAllPayments() {

		String sql = """
				SELECT
				    p.*,

				    pm.name AS paymentMethodName,
				    pt.name AS paymentTypeName,

				    u.name AS studentName,

				    c.name AS courseName

				FROM payment p

				LEFT JOIN payment_method pm
				ON p.paymentMethodID = pm.paymentMethodID

				LEFT JOIN enrollment e
				ON p.enrollmentID = e.enrollmentID

				LEFT JOIN payment_type pt
				ON e.paymentTypeID = pt.paymentTypeID

				LEFT JOIN user u
				ON e.userID = u.userID

				LEFT JOIN course c
				ON e.courseID = c.courseID

				ORDER BY p.created_at DESC
				""";

		return jdbc.query(sql, new PaymentRowMapper());

	}

	// admin payment
	public Double getCollectedAmount() {

		String sql = """
				SELECT COALESCE(SUM(amount),0)
				FROM payment
				WHERE status = 'Success'
				""";

		return jdbc.queryForObject(sql, Double.class);

	}

	// installment plan pedning count
	public Integer getPendingPaymentCount() {

		String sql = """
				SELECT COUNT(*)
				FROM installment_plan
				WHERE status = 'Pending'
				""";

		return jdbc.queryForObject(sql, Integer.class);

	}

	public Double getOutstandingAmount() {

		String sql = """

				SELECT COALESCE(SUM(outstanding),0)

				FROM
				(

				    /*
				     FULL PAYMENT
				     */
				    SELECT
				        (
				            e.final_fee -
				            COALESCE(
				                SUM(p.amount),
				                0
				            )
				        ) AS outstanding


				    FROM enrollment e


				    LEFT JOIN payment p
				    ON e.enrollmentID = p.enrollmentID
				    AND p.status='Success'


				    WHERE e.paymentTypeID IN
				    (
				        SELECT paymentTypeID
				        FROM payment_type
				        WHERE name='FULL_PAYMENT'
				    )


				    GROUP BY e.enrollmentID



				    UNION ALL



				    /*
				     INSTALLMENT
				     */
				    SELECT

				        SUM(
				            ip.amount_due -
				            ip.paid_amount
				        )

				        AS outstanding


				    FROM installment_plan ip


				    WHERE ip.status <> 'Paid'


				) temp


				""";

		return jdbc.queryForObject(sql, Double.class);

	}
	
	//admin payment detail history
	public List<PaymentHistoryBean> 
	findPaymentHistoryByEnrollmentId(
	        String enrollmentId
	){

	    String sql = """
	            SELECT

	                p.paymentID,
	                p.amount,
	                p.payment_date,
	                p.transaction_reference,
	                p.status,

	                pm.name AS paymentMethodName


	            FROM payment p


	            LEFT JOIN payment_method pm

	            ON p.paymentMethodID = pm.paymentMethodID


	            WHERE p.enrollmentID = ?


	            ORDER BY p.created_at DESC

	            """;


	    return jdbc.query(
	            sql,
	            new PaymentHistoryRowMapper(),
	            enrollmentId
	    );

	}
	
	
	public List<PaymentBean> searchPayments(
	        String keyword,
	        String paymentType
	){

	    StringBuilder sql = new StringBuilder("""
	            
	            SELECT
	                p.*,

	                pm.name AS paymentMethodName,
	                pt.name AS paymentTypeName,

	                u.name AS studentName,

	                c.name AS courseName


	            FROM payment p


	            LEFT JOIN payment_method pm
	            ON p.paymentMethodID = pm.paymentMethodID


	            LEFT JOIN enrollment e
	            ON p.enrollmentID = e.enrollmentID


	            LEFT JOIN payment_type pt
	            ON e.paymentTypeID = pt.paymentTypeID


	            LEFT JOIN user u
	            ON e.userID = u.userID


	            LEFT JOIN course c
	            ON e.courseID = c.courseID


	            WHERE 1=1

	            """
	    );


	    List<Object> params = new ArrayList<>();


	    if(keyword != null && !keyword.trim().isEmpty()){


	        sql.append("""
	                
	                AND (
	                    p.transaction_reference LIKE ?
	                    OR u.name LIKE ?
	                    OR c.name LIKE ?
	                )

	                """
	        );


	        String search =
	                "%" + keyword + "%";


	        params.add(search);
	        params.add(search);
	        params.add(search);

	    }



	    if(paymentType != null 
	            && !paymentType.trim().isEmpty()){


	        sql.append("""
	                
	                AND pt.name = ?

	                """
	        );


	        params.add(paymentType);

	    }



	    sql.append("""
	            
	            ORDER BY p.created_at DESC

	            """
	    );



	    return jdbc.query(
	            sql.toString(),
	            new PaymentRowMapper(),
	            params.toArray()
	    );


	}

}