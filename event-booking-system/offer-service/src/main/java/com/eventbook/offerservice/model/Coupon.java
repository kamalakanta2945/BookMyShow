package com.eventbook.offerservice.model;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "coupons")
@Data
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private BigDecimal discount; // Can be a fixed amount or a percentage
    private String discountType; // e.g., "FIXED" or "PERCENTAGE"
    private LocalDate expiryDate;
    private int usageLimit;
    private int usageCount;
    private boolean isActive;
}