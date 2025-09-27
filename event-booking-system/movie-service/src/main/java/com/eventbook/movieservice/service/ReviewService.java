package com.eventbook.movieservice.service;

import com.eventbook.movieservice.model.Movie;
import com.eventbook.movieservice.model.Review;
import com.eventbook.movieservice.repository.MovieRepository;
import com.eventbook.movieservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieRepository movieRepository;

    public List<Review> findReviewsByMovieId(Long movieId) {
        return reviewRepository.findByMovieId(movieId);
    }

    public Review saveReview(Long movieId, Review review) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + movieId));
        review.setMovie(movie);
        return reviewRepository.save(review);
    }
}