package com.bookmyshow.userservice.services;

import com.bookmyshow.userservice.config.security.JwtUtil;
import com.bookmyshow.userservice.dto.JwtResponse;
import com.bookmyshow.userservice.dto.LoginRequest;
import com.bookmyshow.userservice.dto.SignUpRequest;
import com.bookmyshow.userservice.dto.OtpRequest;
import com.bookmyshow.userservice.models.Role;
import com.bookmyshow.userservice.models.User;
import com.bookmyshow.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private NotificationService notificationService;

    public User registerUser(SignUpRequest signUpRequest) {
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(Role.CUSTOMER); // Default role
        user.setVerified(false);

        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        notificationService.sendOtpEmail(user.getEmail(), otp);
        return savedUser;
    }

    public void verifyOtp(OtpRequest otpRequest) {
        User user = userRepository.findByEmail(otpRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isVerified()) {
            throw new RuntimeException("User is already verified.");
        }

        if (!user.getOtp().equals(otpRequest.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        if (user.getOtpGeneratedTime().plusMinutes(5).isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }

        user.setVerified(true);
        user.setOtp(null);
        user.setOtpGeneratedTime(null);
        userRepository.save(user);

        notificationService.sendRegistrationSuccessEmail(user.getEmail());
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public JwtResponse loginUser(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        final String token = jwtUtil.generateToken(userDetails);

        return new JwtResponse(token);
    }
}