package com.eventbook.bookingservice.controller;

import com.eventbook.common.dto.ApiResponse;
import com.eventbook.common.dto.BookingDTO;
import com.eventbook.common.exception.ResourceNotFoundException;
import com.eventbook.bookingservice.dto.BookingRequest;
import com.eventbook.bookingservice.model.Booking;
import com.eventbook.bookingservice.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<Booking>>> getAllBookings(Pageable pageable) {
        Page<Booking> bookings = bookingService.findAllBookings(pageable);
        return new ResponseEntity<>(new ApiResponse<>("Bookings fetched successfully", bookings), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Booking>> getBookingById(@PathVariable Long id, Authentication authentication) {
        Booking booking = bookingService.findBookingById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        // Ownership check
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            Long userIdFromToken = jwt.getClaim("userId");
            if (!booking.getUserId().equals(userIdFromToken)) {
                throw new AccessDeniedException("You are not authorized to view this booking.");
            }
        }

        return new ResponseEntity<>(new ApiResponse<>("Booking fetched successfully", booking), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Booking>> createBooking(@Valid @RequestBody BookingRequest bookingRequest, @RequestHeader("User-Id") Long userId) {
        Booking booking = bookingService.createBooking(bookingRequest, userId);
        return new ResponseEntity<>(new ApiResponse<>("Booking created successfully, pending payment", booking), HttpStatus.CREATED);
    }

    @GetMapping("/report")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingDTO>> getBookingsForReport() {
        List<BookingDTO> bookings = bookingService.findAllForReport();
        return ResponseEntity.ok(bookings);
    }
}