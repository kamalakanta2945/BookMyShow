package com.eventbook.movieservice.controller;

import com.eventbook.movieservice.model.Review;
import com.eventbook.movieservice.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public List<Review> getReviewsByMovieId(@RequestParam Long movieId) {
        return reviewService.findReviewsByMovieId(movieId);
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Review> createReview(@RequestParam Long movieId, @RequestBody Review review, @RequestHeader("User-Id") Long userId) {
        // In a real application, you would get the userId from the security context
        // For now, we'll pass it in the header for simplicity
        review.setUserId(userId);
        Review savedReview = reviewService.saveReview(movieId, review);
        return ResponseEntity.ok(savedReview);
    }
}