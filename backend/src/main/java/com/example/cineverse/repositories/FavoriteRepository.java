package com.example.cineverse.repositories;

import com.example.cineverse.models.Favorite;
import com.example.cineverse.models.Movie;
import com.example.cineverse.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

    // Count how many times a movie was favorited
    int countByMovieId(int movieId);

    // Get all favorites for a user
    List<Favorite> findByUserId(int userId);

    // Check if a movie is favorited by a user
    Optional<Favorite> findByUserIdAndMovieId(int userId, int movieId);


}
