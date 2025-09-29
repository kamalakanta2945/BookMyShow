package com.eventbook.eventservice.controller;

import com.eventbook.common.dto.ApiResponse;
import com.eventbook.common.exception.ResourceNotFoundException;
import com.eventbook.eventservice.model.Event;
import com.eventbook.eventservice.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Event>>> getAllEvents(Pageable pageable, @RequestParam(required = false) String searchTerm) {
        Page<Event> events = eventService.findAllEvents(pageable, searchTerm);
        return new ResponseEntity<>(new ApiResponse<>("Events fetched successfully", events), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Event>> getEventById(@PathVariable Long id) {
        Event event = eventService.findEventById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        return new ResponseEntity<>(new ApiResponse<>("Event fetched successfully", event), HttpStatus.OK);
    }

    @PostMapping("/organizer")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<ApiResponse<Event>> createEventByOrganizer(@Valid @RequestBody Event event, @RequestHeader("User-Id") Long organizerId) {
        event.setOrganizerId(organizerId);
        Event createdEvent = eventService.saveEvent(event);
        return new ResponseEntity<>(new ApiResponse<>("Event created successfully", createdEvent), HttpStatus.CREATED);
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Event>> createEventByAdmin(@Valid @RequestBody Event event) {
        Event createdEvent = eventService.saveEvent(event);
        return new ResponseEntity<>(new ApiResponse<>("Event created successfully by admin", createdEvent), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @eventSecurityService.isOwner(authentication, #id)")
    public ResponseEntity<ApiResponse<Event>> updateEvent(@PathVariable Long id, @Valid @RequestBody Event eventDetails) {
        Event updatedEvent = eventService.updateEvent(id, eventDetails);
        return new ResponseEntity<>(new ApiResponse<>("Event updated successfully", updatedEvent), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return new ResponseEntity<>(new ApiResponse<>("Event deleted successfully"), HttpStatus.NO_CONTENT);
    }
}