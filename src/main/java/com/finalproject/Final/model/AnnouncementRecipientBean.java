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
public class AnnouncementRecipientBean {

    private String announcementRecipientID;

    private String announcementID;

    private String userID;

    private boolean read;

    private LocalDateTime readAt;

    private boolean acknowledged;

    private LocalDateTime acknowledgedAt;

    private LocalDateTime createdAt;

    private boolean deleted;

    private String userName;
}
