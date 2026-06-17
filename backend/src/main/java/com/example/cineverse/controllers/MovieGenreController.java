package com.example.cineverse.controllers;

import com.example.cineverse.models.Genre;
import com.example.cineverse.services.MovieGenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/moviegenres")
public class MovieGenreController {

    private final MovieGenreService movieGenreService;

    @Autowired
    public MovieGenreController(MovieGenreService movieGenreService) {
        this.movieGenreService = movieGenreService;
    }

    @GetMapping("/movie/{movieId}")
    public List<Genre> getGenresOfMovie(@PathVariable int movieId) {
        List<Genre> genres = movieGenreService.getGenresOfMovie(movieId);
        return genres.isEmpty() ? null : genres;
    }

    @PostMapping("/movie/{movieId}/genre/{genreName}")
    public boolean addGenreToMovie(@PathVariable int movieId, @PathVariable String genreName) {
        System.out.println("Received movie for addGenreToMovie" + movieId + ":" + genreName);
        boolean added = movieGenreService.addGenreToMovie(movieId, genreName);
        if (!added) {
            return false;
        }
        return true;
    }

    @DeleteMapping("/movie/{movieId}/genre/{genreId}")
    public String removeGenreFromMovie(@PathVariable int movieId, @PathVariable int genreId) {
        movieGenreService.removeGenreFromMovie(movieId, genreId);
        return "Genre removed from movie successfully.";
    }
}
