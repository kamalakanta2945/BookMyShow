package com.eventbook.eventservice.service;

import com.eventbook.eventservice.model.Event;
import com.eventbook.eventservice.model.Showtime;
import com.eventbook.eventservice.repository.EventRepository;
import com.eventbook.eventservice.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShowtimeService {

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private EventRepository eventRepository;

    public List<Showtime> findShowtimesByEventId(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new RuntimeException("Event not found with id: " + eventId);
        }
        return showtimeRepository.findByEventId(eventId);
    }

    public Showtime saveShowtime(Long eventId, Showtime showtime) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));
        showtime.setEvent(event);
        return showtimeRepository.save(showtime);
    }

    public void deleteShowtime(Long showtimeId) {
        if (!showtimeRepository.existsById(showtimeId)) {
            throw new RuntimeException("Showtime not found with id: " + showtimeId);
        }
        showtimeRepository.deleteById(showtimeId);
    }
}