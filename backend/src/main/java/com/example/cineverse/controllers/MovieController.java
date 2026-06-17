package com.example.cineverse.controllers;

import com.example.cineverse.dto.MovieRequest;
import com.example.cineverse.models.Movie;
import com.example.cineverse.repositories.MovieRepository;
import com.example.cineverse.services.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;
    private final MovieRepository movieRepository;

    public MovieController(MovieService movieService,MovieRepository movieRepository) {
        this.movieService = movieService;
        this.movieRepository = movieRepository;
    }

    // Get all movies
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieService.getMovies());
    }

    // Get a movie by ID
    @GetMapping("/{movieId}")
    public ResponseEntity<Movie> getMovieById(@PathVariable int movieId) {
        Movie movie = movieService.getMovieById(movieId);
        if (movie == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(movie);
    }

    // Add a new movie


    @PostMapping(consumes = "application/json")
    public ResponseEntity<String> insertMovie(@RequestBody Movie movie) {
        boolean created = movieService.insertMovie(movie);
        return created
                ? ResponseEntity.ok("Movie added successfully.")
                : ResponseEntity.badRequest().body("Failed to add movie.");
    }

    // Update a movie
    @PutMapping("/{movieId}")
    public ResponseEntity<String> updateMovie(@RequestBody Movie movie) {
        boolean updated = movieService.updateMovie(movie);
        if (updated) return ResponseEntity.ok("Movie updated successfully.");
        return ResponseEntity.badRequest().body("Failed to update movie.");
    }

    // Delete movie by ID
    @DeleteMapping("/{movieId}")
    public ResponseEntity<String> deleteMovieById(@PathVariable int movieId) {
        boolean deleted = movieService.deleteMovieById(movieId);
        if (deleted) return ResponseEntity.ok("Movie deleted.");
        return ResponseEntity.badRequest().body("Movie not found or couldn't be deleted.");
    }

    // Delete movie by Title (case-insensitive)
    @DeleteMapping("/title/{title}")
    public ResponseEntity<String> deleteMovieByTitle(@PathVariable String title) {
        boolean deleted = movieService.deleteMovieByTitle(title);
        if (deleted) return ResponseEntity.ok("Movie(s) deleted.");
        return ResponseEntity.badRequest().body("No movies found with the given title.");
    }
}

