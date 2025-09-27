package com.bookmyshow.userservice.controllers;

import com.bookmyshow.userservice.dto.ProfileUpdateRequest;
import com.bookmyshow.userservice.models.User;
import com.bookmyshow.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<User> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUserProfile(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<User> updateProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ProfileUpdateRequest updateRequest) {
        User updatedUser = userService.updateUserProfile(userDetails.getUsername(), updateRequest);
        return ResponseEntity.ok(updatedUser);
    }
}