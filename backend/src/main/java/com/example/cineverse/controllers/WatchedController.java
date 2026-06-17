package com.example.cineverse.controllers;

import com.example.cineverse.models.Movie;
import com.example.cineverse.models.Watched;
import com.example.cineverse.models.Watchlist;
import com.example.cineverse.services.WatchedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watched")
public class WatchedController {

    private final WatchedService watchedService;

    public WatchedController(WatchedService watchedService) {
        this.watchedService = watchedService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Movie>> getWatchedMovies(@PathVariable int userId) {
        List<Movie> movies = watchedService.getWatchedMovies(userId);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/user/{userId}/movie/{movieId}/exists")
    public ResponseEntity<Boolean> isMovieWatched(@PathVariable int userId, @PathVariable int movieId) {
        boolean exists = watchedService.isMovieWatched(userId, movieId);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/user/{userId}/movie/{movieId}")
    public ResponseEntity<String> addMovieToWatched(@PathVariable int userId, @PathVariable int movieId) {
        Watched added = watchedService.addToWatched(userId, movieId);
        if (added != null) return ResponseEntity.ok("Movie marked as watched.");
        else return ResponseEntity.badRequest().body("Failed to mark movie as watched.");
    }

    @DeleteMapping("/user/{userId}/movie/{movieId}")
    public ResponseEntity<String> removeMovieFromWatched(@PathVariable int userId, @PathVariable int movieId) {
        boolean removed = watchedService.removeWatched(userId, movieId);
        if (removed) return ResponseEntity.ok("Movie removed from watched list.");
        else return ResponseEntity.badRequest().body("Failed to remove movie from watched list.");
    }
}
