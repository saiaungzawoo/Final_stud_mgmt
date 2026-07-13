package com.finalproject.Final.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserBean {
<<<<<<< Updated upstream

    private int id;
=======
  
  private String userID;

  private String roleID;
@NotBlank(message="Name is required")
@Pattern(
      regexp = "^[A-Z][a-zA-Z ]*$",
      message = "Name must start with a capital letter"
  )
  private String name;
>>>>>>> Stashed changes

    private int roleId;

    private String name;
    private String email;
    private String password;

    private String phoneNo;
    private String address;

    private LocalDate dob;
    private String gender;

    private LocalDateTime createdAt;

    private boolean isActive;

    private String filePath;
}