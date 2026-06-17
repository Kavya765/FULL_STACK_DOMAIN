package com.example.cineverse.services;

import com.example.cineverse.models.Movie;
import com.example.cineverse.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public boolean insertMovie(Movie movie) {
        try {
            movieRepository.save(movie);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update existing movie - only fields that are not null or empty will be updated
    public boolean updateMovie(Movie updatedMovie) {
        if (updatedMovie == null) {
            return false; // No movie or no ID provided
        }

        Optional<Movie> optMovie = Optional.ofNullable(movieRepository.findById(updatedMovie.getMovieId()));

        if (optMovie.isEmpty()) {
            return false; // Movie not found
        }

        try {
            Movie existingMovie = optMovie.get();

            // Update fields only if new value is provided (non-null and for strings non-empty)
            if (updatedMovie.getTitle() != null && !updatedMovie.getTitle().isEmpty()) {
                existingMovie.setTitle(updatedMovie.getTitle());
            }

            if (updatedMovie.getReleaseDate() != null) {
                existingMovie.setReleaseDate(updatedMovie.getReleaseDate());
            }

            if (updatedMovie.getPosterUrl() != null && !updatedMovie.getPosterUrl().isEmpty()) {
                existingMovie.setPosterUrl(updatedMovie.getPosterUrl());
            }

            if (updatedMovie.getRated() != null && !updatedMovie.getRated().isEmpty()) {
                existingMovie.setRated(updatedMovie.getRated());
            }

            if (updatedMovie.getDirector() != null && !updatedMovie.getDirector().isEmpty()) {
                existingMovie.setDirector(updatedMovie.getDirector());
            }

            if (updatedMovie.getActors() != null && !updatedMovie.getActors().isEmpty()) {
                existingMovie.setActors(updatedMovie.getActors());
            }

            if (updatedMovie.getPlot() != null && !updatedMovie.getPlot().isEmpty()) {
                existingMovie.setPlot(updatedMovie.getPlot());
            }

            existingMovie.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

            movieRepository.save(existingMovie);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // Delete movie by title (case-insensitive)
    public boolean deleteMovieByTitle(String title) {
        try {
            // Find movies by title ignoring case
            List<Movie> movies = movieRepository.findByTitleIgnoreCase(title);
            if (movies.isEmpty()) return false;

            movieRepository.deleteAll(movies);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete movie by ID
    public boolean deleteMovieById(int movieId) {
        try {
            if (!movieRepository.existsById(movieId)) {
                return false;
            }
            movieRepository.deleteById(movieId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get all movies
    public List<Movie> getMovies() {
        return movieRepository.findAll();
    }

    // Get movie by ID
    public Movie getMovieById(int movieId) {
        return movieRepository.findById(movieId);
    }
}

