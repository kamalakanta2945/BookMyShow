package com.eventbook.theatreservice.controller;

import com.eventbook.common.dto.ApiResponse;
import com.eventbook.theatreservice.model.Screen;
import com.eventbook.theatreservice.service.ScreenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/screens")
public class ScreenController {

    @Autowired
    private ScreenService screenService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Screen>>> getScreensByTheatreId(@RequestParam Long theatreId) {
        List<Screen> screens = screenService.findScreensByTheatreId(theatreId);
        return new ResponseEntity<>(new ApiResponse<>("Screens fetched successfully", screens), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Screen>> createScreen(@RequestParam Long theatreId, @Valid @RequestBody Screen screen) {
        Screen savedScreen = screenService.saveScreen(theatreId, screen);
        return new ResponseEntity<>(new ApiResponse<>("Screen created successfully", savedScreen), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteScreen(@PathVariable Long id) {
        screenService.deleteScreen(id);
        return new ResponseEntity<>(new ApiResponse<>("Screen deleted successfully"), HttpStatus.NO_CONTENT);
    }
}