package com.eventbook.offerservice.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "offers")
@Data
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String bannerUrl; // For promotional banners
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;
}