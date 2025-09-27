package com.eventbook.eventservice.controller;

import com.eventbook.eventservice.model.Showtime;
import com.eventbook.eventservice.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Showtime> getShowtimesByEventId(@RequestParam Long eventId) {
        return showtimeService.findShowtimesByEventId(eventId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or @eventSecurityService.isOwner(authentication, #eventId)")
    public ResponseEntity<Showtime> createShowtime(@RequestParam Long eventId, @RequestBody Showtime showtime) {
        try {
            Showtime savedShowtime = showtimeService.saveShowtime(eventId, showtime);
            return ResponseEntity.ok(savedShowtime);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @eventSecurityService.isOwner(authentication, T(com.eventbook.eventservice.model.Showtime).#id)")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
        // Note: The ownership check for deletion is more complex. A real implementation
        // would fetch the showtime, get its eventId, and then check ownership.
        // This SpEL expression is a simplification and may need a helper method in EventSecurityService.
        try {
            showtimeService.deleteShowtime(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}