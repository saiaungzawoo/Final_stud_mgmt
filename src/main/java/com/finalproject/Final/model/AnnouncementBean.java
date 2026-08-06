 package com.finalproject.Final.model;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnnouncementBean {

    private String announcementID;

    private String createdByID;

    @NotBlank(message = "Please select course")
    private String courseID;
    
    private String courseName;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    private String targetType;

    private String priority;

    private boolean published;

    private LocalDateTime publishDate;

    @NotNull(message = "Expiry date is required")
    @Future(message = "Expiry date must be in the future")
    private LocalDateTime expiryDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    
    
    
    
}