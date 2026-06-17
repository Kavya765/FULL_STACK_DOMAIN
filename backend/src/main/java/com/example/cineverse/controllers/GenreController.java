package com.example.cineverse.controllers;

import com.example.cineverse.models.Genre;
import com.example.cineverse.services.GenreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    // Get all genres
    @GetMapping
    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }

    // Get genre by ID
    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable int id) {
        Genre genre = genreService.getGenreById(id);
        return genre; // returns null if not found, resulting in empty 200 response body
    }

    // Create or update a genre
    @PostMapping
    public Genre saveGenre(@RequestBody Genre genre) {
        return genreService.saveGenre(genre);
    }

    // Delete a genre by ID
    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable int id) {
        genreService.deleteGenreById(id);
        // void return — returns HTTP 200 OK by default
    }
}
