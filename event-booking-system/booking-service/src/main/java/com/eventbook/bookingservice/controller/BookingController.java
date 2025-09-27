package com.eventbook.bookingservice.controller;

import com.eventbook.bookingservice.dto.BookingRequest;
import com.eventbook.bookingservice.model.Booking;
import com.eventbook.bookingservice.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Booking> getAllBookings() {
        return bookingService.findAllBookings();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')") // A customer should only be able to see their own bookings
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        // Additional logic would be needed to check if the authenticated user owns this booking
        return bookingService.findBookingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest bookingRequest, @RequestHeader("User-Id") Long userId) {
        try {
            Booking booking = bookingService.createBooking(bookingRequest, userId);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            // Log the exception
            return ResponseEntity.badRequest().build();
        }
    }
}