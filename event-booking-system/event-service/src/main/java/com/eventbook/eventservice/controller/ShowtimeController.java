package com.eventbook.eventservice.controller;

import com.eventbook.common.dto.ApiResponse;
import com.eventbook.eventservice.model.Showtime;
import com.eventbook.eventservice.service.ShowtimeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/showtimes")
public class ShowtimeController {

    @Autowired
    private ShowtimeService showtimeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Showtime>>> getShowtimesByEventId(@RequestParam Long eventId) {
        List<Showtime> showtimes = showtimeService.findShowtimesByEventId(eventId);
        return new ResponseEntity<>(new ApiResponse<>("Showtimes fetched successfully", showtimes), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or @eventSecurityService.isOwner(authentication, #eventId)")
    public ResponseEntity<ApiResponse<Showtime>> createShowtime(@RequestParam Long eventId, @Valid @RequestBody Showtime showtime) {
        Showtime savedShowtime = showtimeService.saveShowtime(eventId, showtime);
        return new ResponseEntity<>(new ApiResponse<>("Showtime created successfully", savedShowtime), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @eventSecurityService.isOwner(authentication, T(com.eventbook.eventservice.model.Showtime).#id)")
    public ResponseEntity<ApiResponse<Void>> deleteShowtime(@PathVariable Long id) {
        // Note: The ownership check for deletion is more complex.
        // This is a simplified placeholder.
        showtimeService.deleteShowtime(id);
        return new ResponseEntity<>(new ApiResponse<>("Showtime deleted successfully"), HttpStatus.NO_CONTENT);
    }
}