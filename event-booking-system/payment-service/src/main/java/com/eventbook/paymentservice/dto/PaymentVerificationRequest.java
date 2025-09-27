package com.eventbook.paymentservice.dto;

import lombok.Data;

@Data
public class PaymentVerificationRequest {
    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
    private Long bookingId; // To link the payment back to our internal booking
}