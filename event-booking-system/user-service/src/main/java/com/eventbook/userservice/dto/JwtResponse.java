package com.eventbook.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private java.util.List<String> roles;

    public JwtResponse(String accessToken) {
        this.token = accessToken;
    }
}