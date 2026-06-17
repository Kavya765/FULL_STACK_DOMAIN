package com.example.cineverse.repositories;

import com.example.cineverse.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    Movie findById(int id);
    // Custom query method to find by title (case-insensitive)
    List<Movie> findByTitleIgnoreCase(String title);

    // Find all movies directed by a specific director
    List<Movie> findByDirectorIgnoreCase(String director);


}
