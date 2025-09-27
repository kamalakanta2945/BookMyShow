package com.bookmyshow.userservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your OTP for BookMyShow Registration");
        message.setText("Your OTP is: " + otp);
        mailSender.send(message);
    }

    public void sendRegistrationSuccessEmail(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Registration Successful!");
        message.setText("Welcome to BookMyShow! Your registration was successful.");
        mailSender.send(message);
    }
}