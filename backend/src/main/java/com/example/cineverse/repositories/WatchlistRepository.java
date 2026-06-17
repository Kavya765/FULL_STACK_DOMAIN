package com.example.cineverse.repositories;

import com.example.cineverse.models.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchlistRepository extends JpaRepository<Watchlist, Integer> {

    // Find all watchlist entries by user id
    List<Watchlist> findByUserId(int userId);

    // Check if a watchlist entry exists by user id and movie id
    boolean existsByUserIdAndMovieId(int userId, int movieId);

    // Delete a watchlist entry by user id and movie id
    int deleteByUserIdAndMovieId(int userId, int movieId);
}
