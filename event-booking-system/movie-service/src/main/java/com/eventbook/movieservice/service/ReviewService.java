package com.eventbook.movieservice.service;

import com.eventbook.movieservice.model.Movie;
import com.eventbook.movieservice.model.Review;
import com.eventbook.movieservice.repository.MovieRepository;
import com.eventbook.movieservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    public Page<Review> findReviewsByMovieId(Long movieId, Pageable pageable) {
        return reviewRepository.findByMovieId(movieId, pageable);
    }

    public Review saveReview(Long movieId, Review review) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + movieId));
        review.setMovie(movie);
        return reviewRepository.save(review);
    }
}