package com.finalproject.Final.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.InstallmentPlanBean;
import com.finalproject.Final.model.InstallmentRuleItemBean;

@Repository
public class InstallmentPlanRepository {
	
	  @Autowired
	    private JdbcTemplate jdbc;
	  
	  public void createPlan(
		        String enrollmentId,
		        InstallmentRuleItemBean item
		) {
		  
//		  System.out.println("INSERTING...");
//		  System.out.println(enrollmentId);
//		  System.out.println(item.getInstallmentRuleItemId());
//		  System.out.println(item.getInstallmentNumber());
//		  System.out.println(item.getAmount());
//		  System.out.println(item.getDueDate());

		    String sql = """
		        INSERT INTO installment_plan
		        (
		            installmentPlanID,
		            enrollmentID,
		            installmentRuleItemID,
		            installment_number,
		            amount_due,
		            due_date,
		            paid_amount,
		            status,
		            created_at,
		            updated_at
		        )
		        VALUES
		        (
		            ?,?,?,?,?,?,
		            0,
		            'Pending',
		            NOW(),
		            NOW()
		        )
		        """;

		    jdbc.update(
		            sql,
		            UUID.randomUUID().toString(),
		            enrollmentId,
		            item.getInstallmentRuleItemId(),
		            item.getInstallmentNumber(),
		            item.getAmount(),
		            item.getDueDate()
		    );

		}
	  
	  public void markAsPaid(
		        String installmentPlanId
		) {

		    String sql = """
		        UPDATE installment_plan
		        SET
		            paid_amount = amount_due,
		            status = 'Paid',
		            paid_at = NOW(),
		            updated_at = NOW()
		        WHERE installmentPlanID = ?
		        """;

		    jdbc.update(sql, installmentPlanId);

		}
	  
	  public InstallmentPlanBean getFirstPending(
		        String enrollmentId
		) {

		    String sql = """
		        SELECT *
		        FROM installment_plan
		        WHERE enrollmentID = ?
		        AND status='Pending'
		        ORDER BY installment_number
		        LIMIT 1
		        """;

		    List<InstallmentPlanBean> list =
		            jdbc.query(
		                    sql,
		                    new InstallmentPlanRowMapper(),
		                    enrollmentId
		            );

		    return list.isEmpty()
		            ? null
		            : list.get(0);

		}
	  
	  public InstallmentPlanBean findById(
		        String installmentPlanId
		) {

		    String sql = """
		        SELECT *
		        FROM installment_plan
		        WHERE installmentPlanID = ?
		        """;

		    List<InstallmentPlanBean> list =
		            jdbc.query(
		                    sql,
		                    new InstallmentPlanRowMapper(),
		                    installmentPlanId
		            );

		    return list.isEmpty()
		            ? null
		            : list.get(0);

		}
	  
	  public List<InstallmentPlanBean> findByEnrollmentId(
		        String enrollmentId
		) {

		    String sql = """
		        SELECT *
		        FROM installment_plan
		        WHERE enrollmentID = ?
		        ORDER BY installment_number
		        """;

		    return jdbc.query(
		            sql,
		            new InstallmentPlanRowMapper(),
		            enrollmentId
		    );

		}

	  public boolean existsByEnrollmentId(
		        String enrollmentId
		){

		    String sql = """
		        SELECT COUNT(*)
		        FROM installment_plan
		        WHERE enrollmentID = ?
		        """;

		    Integer count =
		            jdbc.queryForObject(
		                    sql,
		                    Integer.class,
		                    enrollmentId
		            );

		    return count != null && count > 0;

		}
}
