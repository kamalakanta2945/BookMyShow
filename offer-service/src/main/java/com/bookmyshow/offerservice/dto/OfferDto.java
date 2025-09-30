package com.bookmyshow.offerservice.dto;

import lombok.Data;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
public class OfferDto {

    @NotBlank(message = "Offer code cannot be blank")
    private String offerCode;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Discount percentage cannot be null")
    @Positive(message = "Discount percentage must be positive")
    private Double discountPercentage;

    @NotNull(message = "Max discount cannot be null")
    @Positive(message = "Max discount must be positive")
    private Double maxDiscount;

    @NotNull(message = "Expiry date cannot be null")
    @FutureOrPresent(message = "Expiry date must be in the present or future")
    private LocalDate expiryDate;

    private boolean isActive = true;
}