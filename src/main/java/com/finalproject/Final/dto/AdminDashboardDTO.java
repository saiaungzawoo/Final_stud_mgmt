package com.finalproject.Final.dto;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AdminDashboardDTO {

	  private int totalCourses;

	    private int totalStudents;

	    private double totalPayments;

	    private int pendingPayments;
}
