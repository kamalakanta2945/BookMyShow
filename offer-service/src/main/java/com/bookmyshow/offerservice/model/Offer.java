package com.bookmyshow.offerservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Offer implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String offerCode;
    private String description;
    private Double discountPercentage;
    private Double maxDiscount;
    private LocalDate expiryDate;
    private boolean isActive;
}