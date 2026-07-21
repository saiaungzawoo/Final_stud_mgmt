package com.finalproject.Final.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.Final.dto.AdminDashboardDTO;
import com.finalproject.Final.dto.AdminPaymentDTO;
import com.finalproject.Final.repository.AdminDashboardRepository;
import com.finalproject.Final.repository.AdminPaymentRepository;


@Service
public class AdminDashboardService {


	@Autowired
    private AdminDashboardRepository repository;
	
	@Autowired	
	private AdminPaymentRepository adminPayRepo;
	
	
	
    public AdminDashboardDTO getDashboardData() {


        int totalCourses =
                repository.countCourses();


        int totalStudents =
                repository.countStudents();


        double totalPayments =
                repository.totalSuccessfulPayments();


        int pendingPayments =
                repository.countPendingPayments();



        return new AdminDashboardDTO(
                totalCourses,
                totalStudents,
                totalPayments,
                pendingPayments
        );

    }
    
    public List<AdminPaymentDTO> getRecentPayments() {


        return adminPayRepo.getRecentPayments();

    }

}