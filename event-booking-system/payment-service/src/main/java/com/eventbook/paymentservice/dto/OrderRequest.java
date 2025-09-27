package com.eventbook.paymentservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRequest {
    private Long bookingId;
    private BigDecimal amount;
    private String currency; // e.g., "INR"
}