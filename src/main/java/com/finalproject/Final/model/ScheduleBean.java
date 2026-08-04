package com.finalproject.Final.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleBean {
	
	private String scheduleId;
	private String courseId;
	private LocalDate scheduleDate;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime startTime;
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime endTime;
	
	@NotBlank(message = "Room is required.")
	@Size(max = 50, message = "Room must not exceed 50 characters.")
	
	private String room;
	private String topic;
	private String status;
	private LocalDateTime createdAt;
	  private LocalDate startDate;

	  @NotEmpty(message = "Please choose at least one repeat day.")
	  private List<String> repeatDays;
	    private String topicPrefix;
	    private boolean attendanceMarked;

	    // Calendar color control
	    private String attendanceStatus;
	    private String courseName;
}
