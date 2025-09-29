package com.eventbook.eventservice.service;

import com.eventbook.common.exception.ResourceNotFoundException;
import com.eventbook.eventservice.model.Event;
import com.eventbook.eventservice.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventService {

    public static final String EVENTS_CACHE = "events";
    public static final String EVENT_CACHE = "event";

    @Autowired
    private EventRepository eventRepository;

    @Cacheable(value = EVENTS_CACHE, key = "{ #pageable, #searchTerm }")
    public Page<Event> findAllEvents(Pageable pageable, String searchTerm) {
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            Specification<Event> spec = (root, query, criteriaBuilder) -> {
                String pattern = "%" + searchTerm.toLowerCase() + "%";
                return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("category")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("location")), pattern)
                );
            };
            return eventRepository.findAll(spec, pageable);
        } else {
            return eventRepository.findAll(pageable);
        }
    }

    @Cacheable(value = EVENT_CACHE, key = "#id")
    public Optional<Event> findEventById(Long id) {
        return eventRepository.findById(id);
    }

    @CacheEvict(value = {EVENTS_CACHE, EVENT_CACHE}, allEntries = true)
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    @CacheEvict(value = {EVENTS_CACHE, EVENT_CACHE}, allEntries = true)
    public Event updateEvent(Long id, Event eventDetails) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));

        event.setTitle(eventDetails.getTitle());
        event.setDescription(eventDetails.getDescription());
        event.setCategory(eventDetails.getCategory());
        event.setLocation(eventDetails.getLocation());
        // organizerId should likely not be changed here

        return eventRepository.save(event);
    }

    @CacheEvict(value = {EVENTS_CACHE, EVENT_CACHE}, allEntries = true)
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new ResourceNotFoundException("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }
}