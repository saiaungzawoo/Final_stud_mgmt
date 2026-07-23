package com.finalproject.Final.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {


    @Autowired
    private JavaMailSender mailSender;
    /**
     * Send OTP email
     */
    public void sendOtp(String toEmail, String otp) {


        SimpleMailMessage message = new SimpleMailMessage();


        message.setTo(toEmail);


        message.setSubject(
                "Student Management System - OTP Verification"
        );


        message.setText(
                """
                Hello,

                Your OTP Code is:

                %s

                This OTP is valid for 5 minutes only.
                If you did not request this OTP, please ignore this email.

                Please do not share this code with anyone.

                Thank you,
                Student Management System
                """.formatted(otp)
        );

        mailSender.send(message);

    }

}