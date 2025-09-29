package com.eventbook.offerservice.controller;

import com.eventbook.common.dto.ApiResponse;
import com.eventbook.common.exception.ResourceNotFoundException;
import com.eventbook.offerservice.model.Coupon;
import com.eventbook.offerservice.model.Offer;
import com.eventbook.offerservice.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/offers")
public class OfferController {

    @Autowired
    private OfferService offerService;

    // --- Offer Endpoints ---

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Offer>>> getAllOffers(Pageable pageable) {
        Page<Offer> offers = offerService.findAllOffers(pageable);
        return new ResponseEntity<>(new ApiResponse<>("Offers fetched successfully", offers), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Offer>> createOffer(@Valid @RequestBody Offer offer) {
        Offer createdOffer = offerService.saveOffer(offer);
        return new ResponseEntity<>(new ApiResponse<>("Offer created successfully", createdOffer), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Offer>> updateOffer(@PathVariable Long id, @Valid @RequestBody Offer offerDetails) {
        Offer updatedOffer = offerService.updateOffer(id, offerDetails);
        return new ResponseEntity<>(new ApiResponse<>("Offer updated successfully", updatedOffer), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteOffer(@PathVariable Long id) {
        offerService.deleteOffer(id);
        return new ResponseEntity<>(new ApiResponse<>("Offer deleted successfully"), HttpStatus.NO_CONTENT);
    }

    // --- Coupon Endpoints ---

    @GetMapping("/coupons")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<Coupon>>> getAllCoupons(Pageable pageable) {
        Page<Coupon> coupons = offerService.findAllCoupons(pageable);
        return new ResponseEntity<>(new ApiResponse<>("Coupons fetched successfully", coupons), HttpStatus.OK);
    }

    @GetMapping("/coupons/{code}")
    public ResponseEntity<ApiResponse<Coupon>> getCouponByCode(@PathVariable String code) {
        Coupon coupon = offerService.findCouponByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with code: " + code));
        return new ResponseEntity<>(new ApiResponse<>("Coupon fetched successfully", coupon), HttpStatus.OK);
    }

    @GetMapping("/coupons/validate/{code}")
    public ResponseEntity<ApiResponse<Boolean>> validateCoupon(@PathVariable String code) {
        boolean isValid = offerService.validateCoupon(code);
        return new ResponseEntity<>(new ApiResponse<>("Coupon validation status", isValid), HttpStatus.OK);
    }

    @PostMapping("/coupons")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Coupon>> createCoupon(@Valid @RequestBody Coupon coupon) {
        Coupon createdCoupon = offerService.saveCoupon(coupon);
        return new ResponseEntity<>(new ApiResponse<>("Coupon created successfully", createdCoupon), HttpStatus.CREATED);
    }

    @PutMapping("/coupons/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Coupon>> updateCoupon(@PathVariable Long id, @Valid @RequestBody Coupon couponDetails) {
        Coupon updatedCoupon = offerService.updateCoupon(id, couponDetails);
        return new ResponseEntity<>(new ApiResponse<>("Coupon updated successfully", updatedCoupon), HttpStatus.OK);
    }

    @DeleteMapping("/coupons/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteCoupon(@PathVariable Long id) {
        offerService.deleteCoupon(id);
        return new ResponseEntity<>(new ApiResponse<>("Coupon deleted successfully"), HttpStatus.NO_CONTENT);
    }
}