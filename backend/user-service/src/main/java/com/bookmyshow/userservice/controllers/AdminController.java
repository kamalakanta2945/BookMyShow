package com.bookmyshow.userservice.controllers;

import com.bookmyshow.userservice.models.User;
import com.bookmyshow.userservice.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PutMapping("/approve-organizer/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> approveOrganizer(@PathVariable Long userId) {
        User user = adminService.approveOrganizer(userId);
        return ResponseEntity.ok(user);
    }
}