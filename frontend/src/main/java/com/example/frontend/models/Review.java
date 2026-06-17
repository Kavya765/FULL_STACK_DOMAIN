package com.example.frontend.models;

import java.sql.Timestamp;

public class Review {
    private int reviewId;
    private User user;
    private Movie movie;
    private String username;
    private String reviewText;
    private double rating;
    private Timestamp created_at;
    private Timestamp updated_at;

    public Review() {}

    public Review(int reviewId, User user, Movie movie, String username, String reviewText, Timestamp created_at, Timestamp updated_at) {
        this.reviewId = reviewId;
        this.movie = movie;
        this.user = user;
        this.username = username;
        this.reviewText = reviewText;
        this.rating = rating;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getReviewId() { return reviewId; }
    public Movie getMovie() { return movie; }
    public User getUser() { return user; }
    public String getUsername() { return username; }
    public String getReviewText() { return reviewText; }
    public double getRating() { return rating; }
    public Timestamp getCreatedAt() { return created_at; }
    public Timestamp getUpdatedAt() { return updated_at; }
    public int getMovieId() {
        return movie != null ? movie.getId() : 0;
    }

    public int getUserId() {
        return user != null ? user.getUserId() : 0;
    }

    public void setReviewId(int reviewId) { this.reviewId = reviewId; }
    public void setMovie(Movie movie) { this.movie = movie; }
    public void setUser(User user) { this.user = user; }
    public void setUsername(String username) { this.username = username; }
    public void setRating(double rating) { this.rating = rating; }
    public void setReviewText(String reviewText) { this.reviewText = reviewText; }
    public void setCreatedAt(Timestamp created_at) { this.created_at = created_at; }
    public void setUpdatedAt(Timestamp updated_at) { this.updated_at = updated_at; }

}
