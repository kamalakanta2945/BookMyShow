package com.eventbook.theatreservice.service;

import com.eventbook.theatreservice.model.Theatre;
import com.eventbook.theatreservice.repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TheatreService {

    @Autowired
    private TheatreRepository theatreRepository;

    public List<Theatre> findAllTheatres() {
        return theatreRepository.findAll();
    }

    public Optional<Theatre> findTheatreById(Long id) {
        return theatreRepository.findById(id);
    }

    public Theatre saveTheatre(Theatre theatre) {
        return theatreRepository.save(theatre);
    }

    public Theatre updateTheatre(Long id, Theatre theatreDetails) {
        Theatre theatre = theatreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Theatre not found with id: " + id));

        theatre.setName(theatreDetails.getName());
        theatre.setCity(theatreDetails.getCity());
        theatre.setAddress(theatreDetails.getAddress());

        return theatreRepository.save(theatre);
    }

    public void deleteTheatre(Long id) {
        if (!theatreRepository.existsById(id)) {
            throw new RuntimeException("Theatre not found with id: " + id);
        }
        theatreRepository.deleteById(id);
    }
}