package com.eventbook.userservice.service;

import com.eventbook.common.exception.ResourceNotFoundException;
import com.eventbook.userservice.model.UserProfile;
import com.eventbook.userservice.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    public UserProfile getProfileById(Long userId) {
        return userProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User profile not found with id: " + userId));
    }

    public UserProfile createOrUpdateProfile(UserProfile userProfile) {
        // The ID should be set from the authenticated user's token, not the request body,
        // to ensure a user can only update their own profile.
        return userProfileRepository.save(userProfile);
    }
}