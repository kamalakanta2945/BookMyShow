package com.bookmyshow.userservice.controllers;

import com.bookmyshow.userservice.dto.JwtResponse;
import com.bookmyshow.userservice.dto.LoginRequest;
import com.bookmyshow.userservice.dto.OtpRequest;
import com.bookmyshow.userservice.dto.SignUpRequest;
import com.bookmyshow.userservice.models.User;
import com.bookmyshow.userservice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        User user = authService.registerUser(signUpRequest);
        return ResponseEntity.ok("User registered successfully. Please check your email for the OTP.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpRequest otpRequest) {
        authService.verifyOtp(otpRequest);
        return ResponseEntity.ok("OTP verified successfully. Your registration is complete.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.loginUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }
}