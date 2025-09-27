package com.eventbook.eventservice.service;

import com.eventbook.eventservice.model.Event;
import com.eventbook.eventservice.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class EventSecurityService {

    @Autowired
    private EventRepository eventRepository;

    public boolean isOwner(Authentication authentication, Long eventId) {
        // In a real microservices architecture, the user's ID would be extracted
        // from the Authentication object, which is populated by the API Gateway
        // based on the JWT. For this example, we'll assume the principal's name is the user ID.
        String userId = authentication.getName();

        Event event = eventRepository.findById(eventId).orElse(null);
        if (event == null) {
            return false;
        }

        return event.getOrganizerId().equals(Long.parseLong(userId));
    }
}