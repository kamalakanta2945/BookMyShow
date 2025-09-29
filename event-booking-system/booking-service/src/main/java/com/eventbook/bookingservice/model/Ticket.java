package com.eventbook.bookingservice.model;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Table(name = "tickets")
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String seatNumber; // e.g., A1, B12
    private String qrCode; // URL or data for the QR code

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private Booking booking;
}