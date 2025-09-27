package com.eventbook.theatreservice.controller;

import com.eventbook.theatreservice.model.Screen;
import com.eventbook.theatreservice.service.ScreenService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Screen> getScreensByTheatreId(@RequestParam Long theatreId) {
        return screenService.findScreensByTheatreId(theatreId);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Screen> createScreen(@RequestParam Long theatreId, @RequestBody Screen screen) {
        try {
            Screen savedScreen = screenService.saveScreen(theatreId, screen);
            return ResponseEntity.ok(savedScreen);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteScreen(@PathVariable Long id) {
        try {
            screenService.deleteScreen(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}