package com.eventbook.eventservice.service;

import com.eventbook.eventservice.model.Event;
import com.eventbook.eventservice.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class EventSecurityService {

    @Autowired
    private EventRepository eventRepository;

    public boolean isOwner(Authentication authentication, Long eventId) {
        if (!(authentication.getPrincipal() instanceof Jwt)) {
            return false;
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long userIdFromToken = jwt.getClaim("userId");

        if (userIdFromToken == null) {
            return false;
        }

        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            return false;
        }

        return event.getOrganizerId().equals(userIdFromToken);
    }
}