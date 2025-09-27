package com.eventbook.theatreservice.service;

import com.eventbook.theatreservice.model.Screen;
import com.eventbook.theatreservice.model.Theatre;
import com.eventbook.theatreservice.repository.ScreenRepository;
import com.eventbook.theatreservice.repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScreenService {

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private TheatreRepository theatreRepository;

    public List<Screen> findScreensByTheatreId(Long theatreId) {
        if (!theatreRepository.existsById(theatreId)) {
            throw new RuntimeException("Theatre not found with id: " + theatreId);
        }
        return screenRepository.findByTheatreId(theatreId);
    }

    public Screen saveScreen(Long theatreId, Screen screen) {
        Theatre theatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new RuntimeException("Theatre not found with id: " + theatreId));
        screen.setTheatre(theatre);
        return screenRepository.save(screen);
    }

    public void deleteScreen(Long screenId) {
        if (!screenRepository.existsById(screenId)) {
            throw new RuntimeException("Screen not found with id: " + screenId);
        }
        screenRepository.deleteById(screenId);
    }
}