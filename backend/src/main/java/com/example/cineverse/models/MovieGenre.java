package com.example.cineverse.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;

@Entity
@Table(name = "movie_genres")
public class MovieGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_genres_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Genre genre;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    // Constructors
    public MovieGenre() {}

    public MovieGenre(Movie movie, Genre genre) {
        this.movie = movie;
        this.genre = genre;
    }

    // Getters and setters
    public int getId() { return id; }

    public Movie getMovie() { return movie; }

    public Genre getGenre() { return genre; }

    public Timestamp getCreatedAt() { return createdAt; }

    public void setId(int id) { this.id = id; }

    public void setMovie(Movie movie) { this.movie = movie; }

    public void setGenre(Genre genre) { this.genre = genre; }

    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
