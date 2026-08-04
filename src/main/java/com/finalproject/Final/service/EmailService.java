/*
 * package com.finalproject.Final.service;
 * 
 * 
 * import org.springframework.mail.SimpleMailMessage; import
 * org.springframework.mail.javamail.JavaMailSender; import
 * org.springframework.stereotype.Service;
 * 
 * 
 * 
 * @Service public class EmailService {
 * 
 * 
 * 
 * private final JavaMailSender mailSender;
 * 
 * 
 * 
 * public EmailService( JavaMailSender mailSender ){
 * 
 * this.mailSender = mailSender;
 * 
 * }
 * 
 * 
 * 
 * 
 * 
 * 
 * public void sendOtp( String email, String otp ){
 * 
 * 
 * 
 * SimpleMailMessage message = new SimpleMailMessage();
 * 
 * 
 * 
 * message.setTo(email);
 * 
 * 
 * message.setSubject( "Password Reset OTP" );
 * 
 * 
 * 
 * message.setText(
 * 
 * "Your OTP Code is : " + otp + "\n\n" + "This OTP is valid for 1 minute." +
 * "\n\n" + "If you did not request this, please ignore this email."
 * 
 * );
 * 
 * 
 * 
 * mailSender.send(message);
 * 
 * 
 * }
 * 
 * 
 * 
 * }
 */