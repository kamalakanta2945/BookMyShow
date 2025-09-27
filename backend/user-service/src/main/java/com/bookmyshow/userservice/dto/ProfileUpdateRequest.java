package com.bookmyshow.userservice.dto;

import lombok.Data;

@Data
public class ProfileUpdateRequest {
    private String name;
    // Add other updatable fields here, e.g., avatar, preferences
}