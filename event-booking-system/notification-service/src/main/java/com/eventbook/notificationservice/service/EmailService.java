package com.eventbook.notificationservice.service;

import com.eventbook.notificationservice.dto.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(EmailRequest emailRequest) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setTo(emailRequest.getTo());
        helper.setSubject(emailRequest.getSubject());

        // Prefer HTML content if available, otherwise use plain text
        if (emailRequest.getHtmlBody() != null && !emailRequest.getHtmlBody().isEmpty()) {
            helper.setText(emailRequest.getHtmlBody(), true); // true indicates HTML content
        } else {
            helper.setText(emailRequest.getBody(), false); // false indicates plain text
        }

        mailSender.send(mimeMessage);
    }
}