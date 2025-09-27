package com.bookmyshow.userservice.services;

import com.bookmyshow.userservice.dto.ProfileUpdateRequest;
import com.bookmyshow.userservice.models.User;
import com.bookmyshow.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserProfile(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUserProfile(String email, ProfileUpdateRequest updateRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(updateRequest.getName());
        // For simplicity, not allowing email or password change here.
        // That would require a more complex verification flow.

        return userRepository.save(user);
    }
}