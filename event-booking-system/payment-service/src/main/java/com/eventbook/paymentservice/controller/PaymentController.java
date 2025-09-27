package com.eventbook.paymentservice.controller;

import com.eventbook.paymentservice.dto.OrderRequest;
import com.eventbook.paymentservice.dto.PaymentVerificationRequest;
import com.eventbook.paymentservice.service.PaymentService;
import com.razorpay.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-order")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            Order order = paymentService.createOrder(orderRequest);
            return ResponseEntity.ok(order.toString()); // Sending the order details as a string
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating order: " + e.getMessage());
        }
    }

    @PostMapping("/verify-payment")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> verifyPayment(@RequestBody PaymentVerificationRequest verificationRequest) {
        boolean isVerified = paymentService.verifyPayment(verificationRequest);
        if (isVerified) {
            return ResponseEntity.ok("Payment verified successfully and booking confirmed.");
        } else {
            return ResponseEntity.badRequest().body("Payment verification failed.");
        }
    }
}