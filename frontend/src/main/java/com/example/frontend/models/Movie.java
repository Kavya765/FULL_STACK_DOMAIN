package com.example.frontend.models;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class Movie {
    private int id;
    private String title;
    private String posterUrl;
    private String rated;
    private String director;
    private String plot;
    private LocalDate release_date; // For DATE column
    private Timestamp created_at; // For DATETIME column
    private Timestamp updated_at; // For DATETIME column
    private String actors; // List of actors

    public Movie() {

    }

    public Movie(int movie_id, String actors, Timestamp updated_at, Timestamp created_at, LocalDate release_date, String plot, String director, String rated, String posterUrl, String title) {
        this.id = movie_id;
        this.updated_at = updated_at;
        this.created_at = created_at;
        this.release_date = release_date;
        this.plot = plot;
        this.director = director;
        this.rated = rated;
        this.posterUrl = posterUrl;
        this.title = title;
        this.actors = actors;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }


    public String getrated() { return rated; }
    public void setrated(String rated) { this.rated = rated; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getPlot() { return plot; }
    public void setPlot(String plot) { this.plot = plot; }

    public LocalDate getReleaseDate() { return release_date; }
    public void setReleaseDate(LocalDate release_date) { this.release_date = release_date; }

    public Timestamp getCreatedAt() { return created_at; }
    public void setCreatedAt(Timestamp created_at) { this.created_at = created_at; }

    public Timestamp getUpdatedAt() { return updated_at; }
    public void setUpdatedAt(Timestamp updated_at) { this.updated_at = updated_at; }

    public String getActors() { return actors; }
    public void setActors(String actors) { this.actors =  actors; }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", rated='" + rated + '\'' +
                ", director='" + director + '\'' +
                ", plot='" + plot + '\'' +
                ", release_date=" + release_date +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                ", actors=" + actors +
                '}';
    }
}
