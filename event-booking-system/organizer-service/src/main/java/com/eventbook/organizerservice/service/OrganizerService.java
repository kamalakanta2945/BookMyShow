package com.eventbook.organizerservice.service;

import com.eventbook.common.exception.ResourceNotFoundException;
import com.eventbook.organizerservice.model.Organizer;
import com.eventbook.organizerservice.repository.OrganizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizerService {

    @Autowired
    private OrganizerRepository organizerRepository;

    public List<Organizer> getAllOrganizers() {
        return organizerRepository.findAll();
    }

    public Optional<Organizer> getOrganizerById(Long id) {
        return organizerRepository.findById(id);
    }

    public Organizer createOrganizer(Organizer organizer) {
        // In a real scenario, you might have logic to check if the user is already an organizer
        return organizerRepository.save(organizer);
    }

    public Organizer approveOrganizer(Long id) {
        Organizer organizer = organizerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organizer not found with id: " + id));
        organizer.setApproved(true);
        return organizerRepository.save(organizer);
    }

    public Organizer updateOrganizer(Long id, Organizer organizerDetails) {
        Organizer organizer = organizerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Organizer not found with id: " + id));

        organizer.setCompanyName(organizerDetails.getCompanyName());
        organizer.setContactEmail(organizerDetails.getContactEmail());
        organizer.setContactPhone(organizerDetails.getContactPhone());

        return organizerRepository.save(organizer);
    }
}