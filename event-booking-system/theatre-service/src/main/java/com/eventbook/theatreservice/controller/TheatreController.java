package com.eventbook.theatreservice.controller;

import com.eventbook.common.dto.ApiResponse;
import com.eventbook.common.exception.ResourceNotFoundException;
import com.eventbook.theatreservice.model.Theatre;
import com.eventbook.theatreservice.service.TheatreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/theatres")
public class TheatreController {

    @Autowired
    private TheatreService theatreService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Theatre>>> getAllTheatres(Pageable pageable) {
        Page<Theatre> theatres = theatreService.findAllTheatres(pageable);
        return new ResponseEntity<>(new ApiResponse<>("Theatres fetched successfully", theatres), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Theatre>> getTheatreById(@PathVariable Long id) {
        Theatre theatre = theatreService.findTheatreById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre not found with id: " + id));
        return new ResponseEntity<>(new ApiResponse<>("Theatre fetched successfully", theatre), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Theatre>> createTheatre(@Valid @RequestBody Theatre theatre) {
        Theatre createdTheatre = theatreService.saveTheatre(theatre);
        return new ResponseEntity<>(new ApiResponse<>("Theatre created successfully", createdTheatre), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Theatre>> updateTheatre(@PathVariable Long id, @Valid @RequestBody Theatre theatreDetails) {
        Theatre updatedTheatre = theatreService.updateTheatre(id, theatreDetails);
        return new ResponseEntity<>(new ApiResponse<>("Theatre updated successfully", updatedTheatre), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteTheatre(@PathVariable Long id) {
        theatreService.deleteTheatre(id);
        return new ResponseEntity<>(new ApiResponse<>("Theatre deleted successfully"), HttpStatus.NO_CONTENT);
    }
}