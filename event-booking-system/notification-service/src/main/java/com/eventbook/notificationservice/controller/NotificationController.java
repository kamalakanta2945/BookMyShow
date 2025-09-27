package com.eventbook.notificationservice.controller;

import com.eventbook.notificationservice.dto.EmailRequest;
import com.eventbook.notificationservice.service.EmailService;
import com.eventbook.notificationservice.websocket.NotificationWebsocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationWebsocketHandler websocketHandler;

    @PostMapping("/send-email")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        try {
            emailService.sendEmail(emailRequest);
            return "Email sent successfully!";
        } catch (Exception e) {
            return "Error sending email: " + e.getMessage();
        }
    }

    @PostMapping("/broadcast")
    public String broadcastMessage(@RequestBody String message) {
        try {
            websocketHandler.broadcast(message);
            return "Message broadcast successfully!";
        } catch (Exception e) {
            return "Error broadcasting message: " + e.getMessage();
        }
    }
}