package com.eventbook.paymentservice.controller;

import com.eventbook.common.dto.ApiResponse;
import com.eventbook.paymentservice.dto.OrderRequest;
import com.eventbook.paymentservice.dto.PaymentVerificationRequest;
import com.eventbook.paymentservice.service.PaymentService;
import com.razorpay.Order;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-order")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        try {
            Order order = paymentService.createOrder(orderRequest);
            // Convert Razorpay Order to a Map for a structured JSON response
            Map<String, Object> orderMap = order.toMap();
            return new ResponseEntity<>(new ApiResponse<>("Razorpay order created successfully", orderMap), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(e.getMessage(), null, false), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/verify-payment")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<String>> verifyPayment(@Valid @RequestBody PaymentVerificationRequest verificationRequest) {
        boolean isVerified = paymentService.verifyPayment(verificationRequest);
        if (isVerified) {
            return new ResponseEntity<>(new ApiResponse<>("Payment verified successfully and booking confirmed."), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse<>("Payment verification failed.", null, false), HttpStatus.BAD_REQUEST);
        }
    }
}