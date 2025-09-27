package com.eventbook.notificationservice.dto;

import lombok.Data;

@Data
public class EmailRequest {
    private String to;
    private String subject;
    private String body; // Plain text body
    private String htmlBody; // Optional HTML body
}