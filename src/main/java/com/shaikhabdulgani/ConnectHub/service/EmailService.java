package com.shaikhabdulgani.ConnectHub.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
@EnableAsync
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    private void sendMail(String to, String sub, String body){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("project3536.1@gmail.com");
        message.setTo(to);
        message.setText(body);
        message.setSubject(sub);

        mailSender.send(message);
    }

    @Async
    public void sendVerificationLink(String userId,String to,String token){
        String link = String.format("http://localhost:8080/api/auth/verify-account/%s?token=%s",userId,token);
        String message = "To verify your email please click the following link\n"+link;
        String subject = "Verify your email address";
        sendMail(to,subject,message);
    }

    @Async
    public void sendResetOtp(String email, int otp) {
        String message = "This is your reset otp "+otp+" please do not share your otp with anyone. The OTP will expire in 10 minutes from when it was requested";
        String subject = "OTP for password reset request";
        sendMail(email,subject,message);
    }
}
