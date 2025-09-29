package com.eventbook.organizerservice.service;

import com.eventbook.organizerservice.model.Organizer;
import com.eventbook.organizerservice.repository.OrganizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class OrganizerSecurityService {

    @Autowired
    private OrganizerRepository organizerRepository;

    public boolean isOwner(Authentication authentication, Long organizerId) {
        if (!(authentication.getPrincipal() instanceof Jwt)) {
            return false;
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long userIdFromToken = jwt.getClaim("userId");

        if (userIdFromToken == null) {
            return false;
        }

        Organizer organizer = organizerRepository.findById(organizerId).orElse(null);
        if (organizer == null) {
            return false;
        }

        return organizer.getUserId().equals(userIdFromToken);
    }
}