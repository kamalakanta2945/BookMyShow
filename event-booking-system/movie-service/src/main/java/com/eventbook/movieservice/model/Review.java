package com.eventbook.movieservice.model;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "reviews")
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId; // ID of the user who wrote the review
    private String reviewText;
    private int rating; // e.g., 1 to 5 stars

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;
}