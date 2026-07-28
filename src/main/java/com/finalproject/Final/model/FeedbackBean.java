package com.finalproject.Final.model;

import java.sql.Timestamp;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackBean {

    private String feedbackID;

    private String userID;

    @NotBlank(message = "Course is required.")
    private String courseID;

    private String courseName;
    
    @NotNull(message = "Rating is required.")
    @Min(value = 1, message = "Minimum rating is 1.")
    @Max(value = 5, message = "Maximum rating is 5.")
    private Integer rating;

    @NotBlank(message = "Comment is required.")
    private String comment;

    private Integer isAnonymous;

    private Timestamp createdAt;

    private Timestamp updatedAt;
    
    private String userName;
    // new
    private Integer feedbackGiven;

}