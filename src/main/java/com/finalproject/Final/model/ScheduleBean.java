package com.finalproject.Final.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleBean {

    private String scheduleId;

    @NotBlank(message = "Please select course")
    private String courseId;

    @NotNull(message = "Please select date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate scheduleDate;

    @NotNull(message = "Please select start time")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull(message = "Please select end time")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @NotBlank(message = "Please enter room")
    private String room;

    @NotBlank(message = "Please enter topic")
    private String topic;

    private String status;

    private LocalDateTime createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @NotEmpty(message = "Please select at least one repeat day.")
    private List<String> repeatDays;

    private String topicPrefix;

    private boolean attendanceMarked;

    private String attendanceStatus;

    private String courseName;
}