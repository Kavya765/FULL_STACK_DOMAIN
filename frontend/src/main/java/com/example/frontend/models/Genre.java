package com.example.frontend.models;

import java.sql.Timestamp;

public class Genre {
    private int id;
    private String name;
    private Timestamp created_at;

    public Genre() {}
    public Genre(int genre_id, String name, Timestamp created_at) {
        this.id = genre_id;
        this.name = name;
        this.created_at = created_at;
    }

    public int getGenreId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Timestamp getCreatedAt() {
        return created_at;
    }

    public void setGenreId(int genre_id) {
        this.id = genre_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(Timestamp created_at) {
        this.created_at = created_at;
    }
    @Override
    public String toString() {
        return name; // This ensures only the genre name appears in the ComboBox
    }
}
