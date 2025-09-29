package com.eventbook.movieservice.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "movies")
@Data
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String genre;
    private String language;
    private int runtime; // in minutes
    private String trailerUrl;
    private String certification;
    private LocalDate releaseDate;
    private String cast; // Could be a comma-separated string or a more complex relation

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
}