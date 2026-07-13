package com.finalproject.Final.service;

import org.springframework.stereotype.Service;
import com.finalproject.Final.repository.UserRepository;
import com.finalproject.Final.model.UserBean;

import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

// Spring Boot Starter Mail ကို မမှီခိုဘဲ Java Standard Mail သုံးရန် Import များ
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ForgotPasswordService {

    @Autowired
    private UserRepository uRepo;

    // OTP များကို Memory ထဲ၌ ယာယီသိမ်းထားရန်
    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();

    // အဆင့် (၁) - OTP ထုတ်ပြီး Email ပိုပေးခြင်း Logic
    public boolean sendOtp(String email) {
        UserBean user = uRepo.findByEmail(email);
        if (user == null) {
            return false; 
        }

        // ၆ လုံးပါသော Random OTP ထုတ်ခြင်း
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(email, otp);

        // 💡 application.properties ကို မသုံးဘဲ ကုဒ်ထဲမှာတင် Gmail အချက်အလက် တိုက်ရိုက်သတ်မှတ်ခြင်း
        final String username = "သင်၏ဂျီမေးလ်@gmail.com"; 
        final String password = "fbqzklcoppgfcwmf"; // သင်ထုတ်ထားသော App Password

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        // Session တည်ဆောက်ခြင်း
        Session session = Session.getInstance(prop, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Password Reset OTP");
            message.setText("သင်၏ Password ပြန်ပြောင်းရန် OTP ကုဒ်မှာ: " + otp + " ဖြစ်ပါသည်။");

            // Email လှမ်းပိုခြင်း
            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false; 
        }
    }

    // အဆင့် (၂) - OTP ကို စစ်ဆေးပြီး Password ပြောင်းပေးခြင်း
    public String verifyAndResetPassword(String email, String otp, String newPassword) {
        String savedOtp = otpStorage.get(email);

        if (savedOtp == null || !savedOtp.equals(otp)) {
            return "Invalid or expired OTP.";
        }

        int result = uRepo.updatePassword(email, newPassword);
        if (result > 0) {
            otpStorage.remove(email); 
            return "SUCCESS";
        } else {
            return "Email not found.";
        }
    }
}