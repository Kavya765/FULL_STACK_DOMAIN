package com.example.cineverse.repositories;

import com.example.cineverse.models.Watched;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchedRepository extends JpaRepository<Watched, Integer> {

    // Find all watched by user - returns Watched entities, you can map to movies
    List<Watched> findByUserId(int userId);

    // Check if movie is watched by user
    boolean existsByUserIdAndMovieId(int userId, int movieId);

    // Delete watched entry by user and movie
    int deleteByUserIdAndMovieId(int userId, int movieId);
}
