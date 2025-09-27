package com.eventbook.paymentservice.model;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long bookingId;
    private BigDecimal amount;
    private String status; // e.g., CREATED, SUCCESS, FAILED
    private String paymentMethod;
    private String transactionId; // From Razorpay
    private LocalDateTime paymentTime;
}