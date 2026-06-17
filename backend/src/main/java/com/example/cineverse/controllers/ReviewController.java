package com.example.cineverse.controllers;

import com.example.cineverse.models.Review;
import com.example.cineverse.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Get reviews for a movie
    @GetMapping("/movie/{movieId}")
    public List<Review> getReviewsByMovie(@PathVariable int movieId) {
        List<Review> reviews = reviewService.getReviewsByMovieId(movieId);
        return reviews;
    }

    // Get reviews by a user
    @GetMapping("/user/{userId}")
    public List<Review> getReviewsByUser(@PathVariable int userId) {
        List<Review> reviews = reviewService.getReviewsByUserId(userId);
        return reviews;
    }

    // Check if a user has reviewed a movie
    @GetMapping("/exists")
    public boolean hasUserReviewedMovie(@RequestParam int userId, @RequestParam int movieId) {
        boolean exists = reviewService.hasUserReviewedMovie(userId, movieId);
        return exists;
    }

    // Get review count for a movie
    @GetMapping("/count/{movieId}")
    public int getReviewCount(@PathVariable int movieId) {
        int count = reviewService.getReviewCountByMovieId(movieId);
        return count;
    }

    // Create or update a review
    @PostMapping
    public Review saveReview(@RequestBody Review review) {
        Review savedReview = reviewService.saveReview(review);
        return savedReview;
    }

    // Get a single review by ID
    @GetMapping("/{reviewId}")
    public Optional<Review> getReviewById(@PathVariable int reviewId) {
        Optional<Review> reviewOpt = reviewService.getReviewById(reviewId);
        return reviewOpt;
    }

    // Delete a review by ID
    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable int reviewId) {
        reviewService.deleteReviewById(reviewId);
    }
}
