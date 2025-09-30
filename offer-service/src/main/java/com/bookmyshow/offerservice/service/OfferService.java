package com.bookmyshow.offerservice.service;

import com.bookmyshow.offerservice.dto.OfferDto;
import com.bookmyshow.offerservice.model.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OfferService {

    Page<Offer> getAllOffers(Pageable pageable, String searchTerm);

    Offer getOfferById(Long id);

    Offer createOffer(OfferDto offerDto);

    Offer updateOffer(Long id, OfferDto offerDto);

    void deleteOffer(Long id);

    void importOffersFromExcel(MultipartFile file) throws IOException;
}