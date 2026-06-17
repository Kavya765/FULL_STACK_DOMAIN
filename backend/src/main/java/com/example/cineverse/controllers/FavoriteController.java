package com.example.cineverse.controllers;

import com.example.cineverse.models.Movie;
import com.example.cineverse.services.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    // 1. Get how many times a movie was favorited
    @GetMapping("/count/{movieId}")
    public ResponseEntity<Integer> getFavoriteCount(@PathVariable int movieId) {
        int count = favoriteService.getFavoriteCount(movieId);
        return ResponseEntity.ok(count);
    }

    // 2. Get all favorited movies for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Movie>> getFavoritedMovies(@PathVariable int userId) {
        List<Movie> movies = favoriteService.getFavoritedMovies(userId);
        return ResponseEntity.ok(movies);
    }

    // 3. Check if a movie is favorited by a user
    @GetMapping("/check")
    public ResponseEntity<Boolean> isMovieFavorited(@RequestParam int userId, @RequestParam int movieId) {
        boolean isFavorited = favoriteService.isMovieFavorited(userId, movieId);
        return ResponseEntity.ok(isFavorited);
    }

    // 4. Add or remove a favorite
    @PostMapping("/update")
    public ResponseEntity<String> updateFavoriteStatus(@RequestParam int userId,
                                                       @RequestParam int movieId,
                                                       @RequestParam boolean add) {
        boolean success = favoriteService.updateFavoriteStatus(userId, movieId, add);
        if (success) {
            return ResponseEntity.ok(add ? "Favorite added." : "Favorite removed.");
        } else {
            return ResponseEntity.badRequest().body("Operation failed. User or Movie not found, or already in desired state.");
        }
    }
}
