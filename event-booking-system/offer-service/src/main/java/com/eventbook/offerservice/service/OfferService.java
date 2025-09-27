package com.eventbook.offerservice.service;

import com.eventbook.offerservice.model.Coupon;
import com.eventbook.offerservice.model.Offer;
import com.eventbook.offerservice.repository.CouponRepository;
import com.eventbook.offerservice.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private CouponRepository couponRepository;

    // Offer methods
    public List<Offer> findAllOffers() {
        return offerRepository.findAll();
    }

    public Offer saveOffer(Offer offer) {
        return offerRepository.save(offer);
    }

    public Offer updateOffer(Long id, Offer offerDetails) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found with id: " + id));
        offer.setTitle(offerDetails.getTitle());
        offer.setDescription(offerDetails.getDescription());
        offer.setBannerUrl(offerDetails.getBannerUrl());
        offer.setStartDate(offerDetails.getStartDate());
        offer.setEndDate(offerDetails.getEndDate());
        offer.setActive(offerDetails.isActive());
        return offerRepository.save(offer);
    }

    public void deleteOffer(Long id) {
        if (!offerRepository.existsById(id)) {
            throw new RuntimeException("Offer not found with id: " + id);
        }
        offerRepository.deleteById(id);
    }


    // Coupon methods
    public Optional<Coupon> findCouponByCode(String code) {
        return couponRepository.findByCode(code);
    }

    public Coupon saveCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    public Coupon updateCoupon(Long id, Coupon couponDetails) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found with id: " + id));
        coupon.setCode(couponDetails.getCode());
        coupon.setDiscount(couponDetails.getDiscount());
        coupon.setDiscountType(couponDetails.getDiscountType());
        coupon.setExpiryDate(couponDetails.getExpiryDate());
        coupon.setUsageLimit(couponDetails.getUsageLimit());
        coupon.setActive(couponDetails.isActive());
        return couponRepository.save(coupon);
    }

    public void deleteCoupon(Long id) {
        if (!couponRepository.existsById(id)) {
            throw new RuntimeException("Coupon not found with id: " + id);
        }
        couponRepository.deleteById(id);
    }

    public boolean validateCoupon(String code) {
        Optional<Coupon> couponOpt = findCouponByCode(code);
        if (couponOpt.isEmpty()) {
            return false; // Coupon does not exist
        }
        Coupon coupon = couponOpt.get();
        return coupon.isActive() &&
               coupon.getExpiryDate().isAfter(LocalDate.now()) &&
               coupon.getUsageCount() < coupon.getUsageLimit();
    }
}