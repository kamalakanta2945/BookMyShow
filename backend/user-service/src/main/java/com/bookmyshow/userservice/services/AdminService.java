package com.bookmyshow.userservice.services;

import com.bookmyshow.userservice.models.Role;
import com.bookmyshow.userservice.models.User;
import com.bookmyshow.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    public User approveOrganizer(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.PENDING_ORGANIZER) {
            throw new RuntimeException("User is not a pending organizer");
        }

        user.setRole(Role.ORGANIZER);
        return userRepository.save(user);
    }
}