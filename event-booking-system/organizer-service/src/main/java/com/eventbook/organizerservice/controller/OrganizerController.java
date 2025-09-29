package com.eventbook.organizerservice.controller;

import com.eventbook.common.dto.ApiResponse;
import com.eventbook.common.exception.ResourceNotFoundException;
import com.eventbook.organizerservice.model.Organizer;
import com.eventbook.organizerservice.service.OrganizerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/organizers")
public class OrganizerController {

    @Autowired
    private OrganizerService organizerService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Organizer>>> getAllOrganizers() {
        List<Organizer> organizers = organizerService.getAllOrganizers();
        return new ResponseEntity<>(new ApiResponse<>("Organizers fetched successfully", organizers), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')") // Further check needed for ownership
    public ResponseEntity<ApiResponse<Organizer>> getOrganizerById(@PathVariable Long id) {
        Organizer organizer = organizerService.getOrganizerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organizer not found with id: " + id));
        return new ResponseEntity<>(new ApiResponse<>("Organizer fetched successfully", organizer), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')") // A customer can apply to become an organizer
    public ResponseEntity<ApiResponse<Organizer>> createOrganizer(@Valid @RequestBody Organizer organizer) {
        Organizer createdOrganizer = organizerService.createOrganizer(organizer);
        return new ResponseEntity<>(new ApiResponse<>("Organizer application submitted successfully", createdOrganizer), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Organizer>> approveOrganizer(@PathVariable Long id) {
        Organizer approvedOrganizer = organizerService.approveOrganizer(id);
        return new ResponseEntity<>(new ApiResponse<>("Organizer approved successfully", approvedOrganizer), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @organizerSecurityService.isOwner(authentication, #id)")
    public ResponseEntity<ApiResponse<Organizer>> updateOrganizer(@PathVariable Long id, @Valid @RequestBody Organizer organizerDetails) {
        Organizer updatedOrganizer = organizerService.updateOrganizer(id, organizerDetails);
        return new ResponseEntity<>(new ApiResponse<>("Organizer updated successfully", updatedOrganizer), HttpStatus.OK);
    }
}