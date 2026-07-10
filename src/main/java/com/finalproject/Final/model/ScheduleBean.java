package com.finalproject.Final.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleBean {
	
	private String scheduleId;
	private String courseId;
	private LocalDate scheduleDate;
	private LocalTime startTime;
	private LocalTime endTime;
	private String room;
	private String topic;
	private String status;
	private LocalDateTime createdAt;

}
