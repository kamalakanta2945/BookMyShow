package com.bookmyshow.offerservice.controller;

import com.bookmyshow.offerservice.dto.ApiResponse;
import com.bookmyshow.offerservice.dto.OfferDto;
import com.bookmyshow.offerservice.model.Offer;
import com.bookmyshow.offerservice.service.OfferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/offers")
public class OfferController {

    private static final Logger log = LoggerFactory.getLogger(OfferController.class);

    @Autowired
    private OfferService offerService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Offer>>> getAllOffers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort,
            @RequestParam(required = false) String search) {

        log.info("GET /api/offers - page: {}, size: {}, sort: {}, search: '{}'", page, size, sort, search);
        String sortField = sort[0];
        Sort.Direction sortDirection = sort.length > 1 && sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortField));

        Page<Offer> offers = offerService.getAllOffers(pageable, search);
        ApiResponse<Page<Offer>> response = new ApiResponse<>(HttpStatus.OK, "Offers retrieved successfully.", offers);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Offer>> getOfferById(@PathVariable Long id) {
        log.info("GET /api/offers/{}", id);
        Offer offer = offerService.getOfferById(id);
        ApiResponse<Offer> response = new ApiResponse<>(HttpStatus.OK, "Offer found.", offer);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Offer>> createOffer(@Valid @RequestBody OfferDto offerDto) {
        log.info("POST /api/offers - creating new offer");
        Offer createdOffer = offerService.createOffer(offerDto);
        ApiResponse<Offer> response = new ApiResponse<>(HttpStatus.CREATED, "Offer created successfully.", createdOffer);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Offer>> updateOffer(@PathVariable Long id, @Valid @RequestBody OfferDto offerDto) {
        log.info("PUT /api/offers/{}", id);
        Offer updatedOffer = offerService.updateOffer(id, offerDto);
        ApiResponse<Offer> response = new ApiResponse<>(HttpStatus.OK, "Offer updated successfully.", updatedOffer);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteOffer(@PathVariable Long id) {
        log.info("DELETE /api/offers/{}", id);
        offerService.deleteOffer(id);
        ApiResponse<Void> response = new ApiResponse<>(HttpStatus.OK, "Offer deleted successfully.", null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/import")
    public ResponseEntity<ApiResponse<Void>> importOffers(@RequestParam("file") MultipartFile file) {
        log.info("POST /api/offers/import - importing from file: {}", file.getOriginalFilename());
        try {
            offerService.importOffersFromExcel(file);
            ApiResponse<Void> response = new ApiResponse<>(HttpStatus.OK, "Offers imported successfully from Excel.", null);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            log.error("Error importing offers from Excel", e);
            ApiResponse<Void> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to import offers: " + e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}