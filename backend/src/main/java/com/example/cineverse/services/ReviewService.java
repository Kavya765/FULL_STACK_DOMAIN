package com.example.cineverse.services;

import com.example.cineverse.models.Review;
import com.example.cineverse.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // Get all reviews for a specific movie
    public List<Review> getReviewsByMovieId(int movieId) {
        return reviewRepository.findByMovieId(movieId);
    }

    // Get all reviews made by a specific user
    public List<Review> getReviewsByUserId(int userId) {
        return reviewRepository.findByUserId(userId);
    }

    // Check if a user has already reviewed a movie
    public boolean hasUserReviewedMovie(int userId, int movieId) {
        return reviewRepository.existsByUserIdAndMovieId(userId, movieId);
    }

    // Count reviews for a specific movie
    public int getReviewCountByMovieId(int movieId) {
        return reviewRepository.countByMovieId(movieId);
    }

    // Add or update a review
    public Review saveReview(Review review) {
        return reviewRepository.save(review);
    }

    // Delete a review by ID
    public void deleteReviewById(int reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    // Find a review by ID
    public Optional<Review> getReviewById(int reviewId) {
        return reviewRepository.findById(reviewId);
    }
}
