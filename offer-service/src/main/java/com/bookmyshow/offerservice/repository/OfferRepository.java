package com.bookmyshow.offerservice.repository;

import com.bookmyshow.offerservice.model.Offer;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OfferRepository {

    List<Offer> findAll(Pageable pageable, String searchTerm);

    int count(String searchTerm);

    Optional<Offer> findById(Long id);

    Offer save(Offer offer);

    int update(Offer offer);

    int deleteById(Long id);

    int[] saveAll(List<Offer> offers);
}