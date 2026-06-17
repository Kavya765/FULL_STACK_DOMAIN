package com.example.cineverse.repositories;

import com.example.cineverse.models.Genre;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {
    boolean existsByName(String name);

    Genre getGenreByName(@NotEmpty(message = "Genre name cannot be empty") String name);
}
