package com.example.cineverse.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Transient
    private String username; // Not stored in the DB, for display only

    @Column(name = "review_text")
    private String reviewText;

    @Column(nullable = false)
    private double rating;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    // Constructors
    public Review() {}

    public Review(Movie movie, User user, String reviewText, double rating) {
        this.movie = movie;
        this.user = user;
        this.reviewText = reviewText;
        this.rating = rating;
    }

    // Getters and setters
    public int getReviewId() { return id; }

    public Movie getMovie() { return movie; }

    public User getUser() { return user; }

    public String getUsername() { return username; }

    public String getReviewText() { return reviewText; }

    public double getRating() { return rating; }

    public Timestamp getCreatedAt() { return createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }

    public void setReviewId(int reviewId) { this.id = reviewId; }

    public void setMovie(Movie movie) { this.movie = movie; }

    public void setUser(User user) { this.user = user; }

    public void setUsername(String username) { this.username = username; }

    public void setReviewText(String reviewText) { this.reviewText = reviewText; }

    public void setRating(double rating) { this.rating = rating; }

    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
