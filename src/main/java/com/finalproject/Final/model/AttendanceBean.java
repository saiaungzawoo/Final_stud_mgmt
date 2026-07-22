package com.finalproject.Final.model;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceBean {

	
	 private String attendanceID;
	    private String scheduleID;
	    private String userID;
	    private String studentName;
	    private AttendanceStatus status;
	    private LocalTime checkInTime;
	    private String remarks;
	    private String markedByID;
	    public enum AttendanceStatus {

	        Present,
	        Absent,
	        Late,
	        Excused

	    }
	    private String topic;

	    private LocalDate scheduleDate;

	    private LocalTime startTime;

	    private LocalTime endTime;
	   
}
