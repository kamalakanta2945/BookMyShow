package com.eventbook.movieservice.controller;

import com.eventbook.common.dto.ApiResponse;
import com.eventbook.common.exception.ResourceNotFoundException;
import com.eventbook.movieservice.model.Movie;
import com.eventbook.movieservice.service.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Movie>>> getAllMovies(Pageable pageable, @RequestParam(required = false) String searchTerm) {
        Page<Movie> movies = movieService.findAllMovies(pageable, searchTerm);
        return new ResponseEntity<>(new ApiResponse<>("Movies fetched successfully", movies), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Movie>> getMovieById(@PathVariable Long id) {
        Movie movie = movieService.findMovieById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
        return new ResponseEntity<>(new ApiResponse<>("Movie fetched successfully", movie), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Movie>> createMovie(@Valid @RequestBody Movie movie) {
        Movie createdMovie = movieService.saveMovie(movie);
        return new ResponseEntity<>(new ApiResponse<>("Movie created successfully", createdMovie), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Movie>> updateMovie(@PathVariable Long id, @Valid @RequestBody Movie movieDetails) {
        Movie updatedMovie = movieService.updateMovie(id, movieDetails);
        return new ResponseEntity<>(new ApiResponse<>("Movie updated successfully", updatedMovie), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return new ResponseEntity<>(new ApiResponse<>("Movie deleted successfully"), HttpStatus.NO_CONTENT);
    }

    @PostMapping("/import")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> importMovies(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse<>("Please upload a non-empty Excel file.", null, false), HttpStatus.BAD_REQUEST);
        }
        try {
            movieService.importMoviesFromExcel(file.getInputStream());
            return new ResponseEntity<>(new ApiResponse<>("Movies imported successfully."), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>("Failed to import movies: " + e.getMessage(), null, false), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}