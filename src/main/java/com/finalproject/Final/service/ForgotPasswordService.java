package com.finalproject.Final.service;
import java.security.SecureRandom;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.finalproject.Final.model.UserBean;
import com.finalproject.Final.repository.UserRepository;


@Service
public class ForgotPasswordService {


    private final UserRepository userRepository;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;


    private final SecureRandom random = new SecureRandom();



    public ForgotPasswordService(
            UserRepository userRepository,
            EmailService emailService,
            PasswordEncoder passwordEncoder
    ) {

        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;

    }




    // Send OTP
    public boolean sendOtp(String email) {


        UserBean user =
                userRepository.findByEmail(email);



        if(user == null) {

            return false;
        }



        String otp = generateOtp();



        userRepository.saveOtpCode(
                email,
                otp
        );



        emailService.sendOtp(
                email,
                otp
        );


        return true;

    }





    // Verify OTP Only
    public String verifyOtp(
            String email,
            String otp
    ) {


        UserBean user =
                userRepository.findByEmail(email);



        if(user == null) {

            return "Email not found.";

        }




        if(user.getOtpCode() == null) {

            return "OTP expired.";

        }





        if(!user.getOtpCode().equals(otp)) {


            return "Invalid OTP.";

        }



        return "SUCCESS";

    }






    // Update Password Only
    public String updatePassword(
            String email,
            String newPassword
    ) {



        String encodedPassword =
                passwordEncoder.encode(newPassword);



        int result =
                userRepository.updatePassword(
                        email,
                        encodedPassword
                );



        if(result > 0) {


            userRepository.clearOtpCode(email);


            return "SUCCESS";

        }



        return "Password update failed.";

    }







    // Generate OTP
    private String generateOtp() {


        int otp =
                random.nextInt(900000)
                + 100000;



        return String.valueOf(otp);

    }


}