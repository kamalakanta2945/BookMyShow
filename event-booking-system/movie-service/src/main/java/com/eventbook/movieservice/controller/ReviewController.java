package com.eventbook.movieservice.controller;

import com.eventbook.common.dto.ApiResponse;
import com.eventbook.movieservice.model.Review;
import com.eventbook.movieservice.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Review>>> getReviewsByMovieId(@RequestParam Long movieId, Pageable pageable) {
        Page<Review> reviews = reviewService.findReviewsByMovieId(movieId, pageable);
        return new ResponseEntity<>(new ApiResponse<>("Reviews fetched successfully", reviews), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Review>> createReview(@RequestParam Long movieId,
                                                            @Valid @RequestBody Review review,
                                                            @RequestHeader("User-Id") Long userId) {
        // In a real application, you would get the userId from the security context
        // For now, we'll pass it in the header for simplicity
        review.setUserId(userId);
        Review savedReview = reviewService.saveReview(movieId, review);
        return new ResponseEntity<>(new ApiResponse<>("Review created successfully", savedReview), HttpStatus.CREATED);
    }
}