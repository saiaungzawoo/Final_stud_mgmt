package com.finalproject.Final.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.Final.model.InstallmentPlanBean;
import com.finalproject.Final.model.InstallmentRuleItemBean;
import com.finalproject.Final.repository.InstallmentPlanRepository;

@Service
public class InstallmentPlanService {
	
	 @Autowired
	    private InstallmentPlanRepository repo;

	    public void createPlan(
	            String enrollmentId,
	            InstallmentRuleItemBean item
	    ){

	        repo.createPlan(
	                enrollmentId,
	                item
	        );

	    }
	    
	    public InstallmentPlanBean getFirstPending(
	            String enrollmentId
	    ){

	        return repo.getFirstPending(enrollmentId);

	    }

	    public void markPaid(
	            String installmentPlanId
	    ){

	        repo.markAsPaid(installmentPlanId);

	    }

	    public InstallmentPlanBean getById(
	            String installmentPlanId
	    ) {

	        return repo.findById(installmentPlanId);

	    }
	    
	    public List<InstallmentPlanBean> getByEnrollmentId(
	            String enrollmentId
	    ) {

	        return repo.findByEnrollmentId(enrollmentId);

	    }
	    
	    public boolean existsByEnrollmentId(
	            String enrollmentId
	    ){

	        return repo.existsByEnrollmentId(
	                enrollmentId
	        );

	    }
}
