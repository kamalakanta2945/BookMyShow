package com.eventbook.bookingservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BookingRequest {
    private Long showtimeId;
    private List<String> seatNumbers;
    private BigDecimal pricePerSeat; // In a real app, this might be fetched from the showtime/theatre service
}