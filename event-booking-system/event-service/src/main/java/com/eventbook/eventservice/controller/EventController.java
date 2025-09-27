package com.eventbook.eventservice.controller;

import com.eventbook.eventservice.model.Event;
import com.eventbook.eventservice.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.findAllEvents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventService.findEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/organizer")
    @PreAuthorize("hasRole('ORGANIZER')")
    public Event createEventByOrganizer(@RequestBody Event event, @RequestHeader("User-Id") Long organizerId) {
        event.setOrganizerId(organizerId);
        // In a real app, you might add a status like PENDING_APPROVAL
        return eventService.saveEvent(event);
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Event createEventByAdmin(@RequestBody Event event) {
        // Admins can create events directly, maybe for their own promotions
        return eventService.saveEvent(event);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @eventSecurityService.isOwner(authentication, #id)")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event eventDetails) {
        try {
            Event updatedEvent = eventService.updateEvent(id, eventDetails);
            return ResponseEntity.ok(updatedEvent);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}