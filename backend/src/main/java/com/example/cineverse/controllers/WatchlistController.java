package com.example.cineverse.controllers;

import com.example.cineverse.models.Movie;
import com.example.cineverse.models.Watchlist;
import com.example.cineverse.services.WatchlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
public class WatchlistController {

    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Movie>> getWatchlistedMovies(@PathVariable int userId) {
        List<Movie> movies = watchlistService.getWatchlistedMovies(userId);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/user/{userId}/movie/{movieId}/exists")
    public ResponseEntity<Boolean> isMovieInWatchlist(@PathVariable int userId, @PathVariable int movieId) {
        boolean exists = watchlistService.isMovieInWatchlist(userId, movieId);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/user/{userId}/movie/{movieId}")
    public ResponseEntity<String> addMovieToWatchlist(@PathVariable int userId, @PathVariable int movieId) {
        Watchlist added = watchlistService.addToWatchlist(userId, movieId);
        if (added != null) return ResponseEntity.ok("Movie added to watchlist.");
        else return ResponseEntity.badRequest().body("Failed to add movie to watchlist.");
    }

    @DeleteMapping("/user/{userId}/movie/{movieId}")
    public ResponseEntity<String> removeMovieFromWatchlist(@PathVariable int userId, @PathVariable int movieId) {
        boolean removed = watchlistService.removeWatchlist(userId, movieId);
        if (removed) return ResponseEntity.ok("Movie removed from watchlist.");
        else return ResponseEntity.badRequest().body("Failed to remove movie from watchlist.");
    }
}
