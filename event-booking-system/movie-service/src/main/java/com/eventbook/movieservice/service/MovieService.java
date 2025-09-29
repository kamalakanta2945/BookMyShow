package com.eventbook.movieservice.service;

import com.eventbook.common.exception.ResourceNotFoundException;
import com.eventbook.movieservice.model.Movie;
import com.eventbook.movieservice.repository.MovieRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    public static final String MOVIES_CACHE = "movies";
    public static final String MOVIE_CACHE = "movie";

    @Autowired
    private MovieRepository movieRepository;

    @Cacheable(value = MOVIES_CACHE, key = "{ #pageable, #searchTerm }")
    public Page<Movie> findAllMovies(Pageable pageable, String searchTerm) {
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            Specification<Movie> spec = (root, query, criteriaBuilder) -> {
                String pattern = "%" + searchTerm.toLowerCase() + "%";
                return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("genre")), pattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("cast")), pattern)
                );
            };
            return movieRepository.findAll(spec, pageable);
        } else {
            return movieRepository.findAll(pageable);
        }
    }

    @Cacheable(value = MOVIE_CACHE, key = "#id")
    public Optional<Movie> findMovieById(Long id) {
        return movieRepository.findById(id);
    }

    @CacheEvict(value = {MOVIES_CACHE, MOVIE_CACHE}, allEntries = true)
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @CacheEvict(value = {MOVIES_CACHE, MOVIE_CACHE}, allEntries = true)
    public Movie updateMovie(Long id, Movie movieDetails) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));

        movie.setTitle(movieDetails.getTitle());
        movie.setDescription(movieDetails.getDescription());
        movie.setGenre(movieDetails.getGenre());
        movie.setLanguage(movieDetails.getLanguage());
        movie.setRuntime(movieDetails.getRuntime());
        movie.setTrailerUrl(movieDetails.getTrailerUrl());
        movie.setCertification(movieDetails.getCertification());
        movie.setReleaseDate(movieDetails.getReleaseDate());
        movie.setCast(movieDetails.getCast());

        return movieRepository.save(movie);
    }

    @CacheEvict(value = {MOVIES_CACHE, MOVIE_CACHE}, allEntries = true)
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movie not found with id: " + id);
        }
        movieRepository.deleteById(id);
    }

    @CacheEvict(value = {MOVIES_CACHE, MOVIE_CACHE}, allEntries = true)
    public void importMoviesFromExcel(InputStream inputStream) {
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            List<Movie> movies = new ArrayList<>();
            for (Row row : sheet) {
                if (row.getRowNum() == 0) { // skip header
                    continue;
                }
                Movie movie = new Movie();
                movie.setTitle(row.getCell(0).getStringCellValue());
                movie.setDescription(row.getCell(1).getStringCellValue());
                movie.setGenre(row.getCell(2).getStringCellValue());
                movie.setLanguage(row.getCell(3).getStringCellValue());
                movie.setRuntime((int) row.getCell(4).getNumericCellValue());
                movie.setTrailerUrl(row.getCell(5).getStringCellValue());
                movie.setCertification(row.getCell(6).getStringCellValue());
                movie.setReleaseDate(row.getCell(7).getLocalDateTimeCellValue().toLocalDate());
                movie.setCast(row.getCell(8).getStringCellValue());
                movies.add(movie);
            }
            movieRepository.saveAll(movies);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
    }
}