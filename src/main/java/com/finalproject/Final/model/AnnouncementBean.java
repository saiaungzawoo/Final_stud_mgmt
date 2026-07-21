package com.finalproject.Final.model;

import java.time.LocalDateTime;

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

    private String courseID;
    
    private String courseName;


    private String title;

    private String content;

    private String targetType;

    private String priority;

    private boolean published;

    private LocalDateTime publishDate;

    private LocalDateTime expiryDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
