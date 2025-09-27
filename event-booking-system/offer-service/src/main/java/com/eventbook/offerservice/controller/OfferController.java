package com.eventbook.offerservice.controller;

import com.eventbook.offerservice.model.Coupon;
import com.eventbook.offerservice.model.Offer;
import com.eventbook.offerservice.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/offers")
public class OfferController {

    @Autowired
    private OfferService offerService;

    // --- Offer Endpoints ---

    @GetMapping
    public List<Offer> getAllOffers() {
        return offerService.findAllOffers();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Offer createOffer(@RequestBody Offer offer) {
        return offerService.saveOffer(offer);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Offer> updateOffer(@PathVariable Long id, @RequestBody Offer offerDetails) {
        try {
            return ResponseEntity.ok(offerService.updateOffer(id, offerDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        try {
            offerService.deleteOffer(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // --- Coupon Endpoints ---

    @GetMapping("/coupons/{code}")
    public ResponseEntity<Coupon> getCouponByCode(@PathVariable String code) {
        return offerService.findCouponByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/coupons/validate/{code}")
    public ResponseEntity<Boolean> validateCoupon(@PathVariable String code) {
        return ResponseEntity.ok(offerService.validateCoupon(code));
    }

    @PostMapping("/coupons")
    @PreAuthorize("hasRole('ADMIN')")
    public Coupon createCoupon(@RequestBody Coupon coupon) {
        return offerService.saveCoupon(coupon);
    }

    @PutMapping("/coupons/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Coupon> updateCoupon(@PathVariable Long id, @RequestBody Coupon couponDetails) {
        try {
            return ResponseEntity.ok(offerService.updateCoupon(id, couponDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/coupons/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        try {
            offerService.deleteCoupon(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}