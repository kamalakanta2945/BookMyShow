package com.eventbook.userservice.controller;

import com.eventbook.common.dto.ApiResponse;
import com.eventbook.userservice.model.UserProfile;
import com.eventbook.userservice.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserProfile>> getCurrentUserProfile(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long userId = jwt.getClaim("userId");
        UserProfile userProfile = userProfileService.getProfileById(userId);
        return new ResponseEntity<>(new ApiResponse<>("Profile fetched successfully", userProfile), HttpStatus.OK);
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserProfile>> updateCurrentUserProfile(@Valid @RequestBody UserProfile profileDetails, Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long userId = jwt.getClaim("userId");
        profileDetails.setId(userId); // Ensure the user can only update their own profile
        UserProfile updatedProfile = userProfileService.createOrUpdateProfile(profileDetails);
        return new ResponseEntity<>(new ApiResponse<>("Profile updated successfully", updatedProfile), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserProfile>> getUserProfileById(@PathVariable Long id) {
        UserProfile userProfile = userProfileService.getProfileById(id);
        return new ResponseEntity<>(new ApiResponse<>("Profile fetched successfully", userProfile), HttpStatus.OK);
    }
}