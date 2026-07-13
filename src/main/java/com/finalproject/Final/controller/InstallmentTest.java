package com.finalproject.Final.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.finalproject.Final.model.InstallmentPlanBean;
import com.finalproject.Final.model.InstallmentRuleBean;
import com.finalproject.Final.model.InstallmentRuleItemBean;
import com.finalproject.Final.service.InstallmentRuleService;
import com.finalproject.Final.service.InstallmentPlanService;
import com.finalproject.Final.service.InstallmentRuleItemService;



@Controller
@RequestMapping("/payment")
public class InstallmentTest {
	@Autowired
	private InstallmentRuleService installmentRuleService;


	@Autowired
	private InstallmentRuleItemService installmentRuleItemService;
	
	@Autowired
	private InstallmentPlanService installmentPlanService;
	
	@GetMapping("/test-installment/{courseId}")
	public String testInstallment(
	        @PathVariable String courseId
	) {


	    InstallmentRuleBean rule =
	            installmentRuleService.getByCourseId(courseId);


	    System.out.println("========== INSTALLMENT RULE ==========");


	    if(rule == null) {

	        System.out.println("No installment rule found");

	        return "test";

	    }


	    System.out.println(
	            "Rule ID: "
	            + rule.getInstallmentRuleId()
	    );


	    System.out.println(
	            "Course ID: "
	            + rule.getCourseId()
	    );


	    System.out.println(
	            "Name: "
	            + rule.getName()
	    );


	    System.out.println(
	            "Count: "
	            + rule.getInstallmentCount()
	    );



	    List<InstallmentRuleItemBean> items =
	            installmentRuleItemService
	            .getByRuleId(
	                rule.getInstallmentRuleId()
	            );

	    //plan test
	    String enrollmentId =
	            "6224ca2b-9bd2-4ae4-a30c-ee376ae5f106";

	    System.out.println("Items size = " + items.size());
	    for(InstallmentRuleItemBean item : items){
	    	System.out.println("Creating installment " + item.getInstallmentNumber());

	        installmentPlanService.createPlan(
	                enrollmentId,
	                item
	        );

	    }


	    System.out.println(
	            "========== INSTALLMENT ITEMS =========="
	    );


	    for(InstallmentRuleItemBean item : items) {


	        System.out.println(
	            "Installment "
	            + item.getInstallmentNumber()
	            +
	            " Amount: "
	            + item.getAmount()
	            +
	            " Due: "
	            + item.getDueDate()
	        );


	    }


	    return "test";

	}
	
	@GetMapping("/test-plans/{enrollmentId}")
	public String testPlans(
	        @PathVariable String enrollmentId
	){

	    List<InstallmentPlanBean> plans =
	            installmentPlanService.getByEnrollmentId(
	                    enrollmentId
	            );

	    System.out.println("========== PLANS ==========");

	    for(InstallmentPlanBean plan : plans){

	        System.out.println(
	                "No : " + plan.getInstallmentNumber()
	        );

	        System.out.println(
	                "Amount : " + plan.getAmountDue()
	        );

	        System.out.println(
	                "Status : " + plan.getStatus()
	        );

	        System.out.println(
	                "Due : " + plan.getDueDate()
	        );

	        System.out.println("---------------------");

	    }

	    return "test";

	}

}
