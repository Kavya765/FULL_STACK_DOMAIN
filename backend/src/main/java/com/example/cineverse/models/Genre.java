package com.example.cineverse.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.sql.Timestamp;

@Entity
@Table(name = "genres")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private int id;

    @NotEmpty(message = "Genre name cannot be empty")
    @Column(name = "name")
    private String name;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    public Genre() { }

    public Genre(int genreId, String name, Timestamp createdAt) {
        this.id = genreId;
        this.name = name;
        this.createdAt = createdAt;
    }

    public int getGenreId() {
        return id;
    }

    public void setGenreId(int genreId) {
        this.id = genreId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "id: " + id + ", name: " + name + ", createdAt: " + createdAt;
    }
}
