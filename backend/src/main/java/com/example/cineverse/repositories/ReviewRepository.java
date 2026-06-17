package com.example.cineverse.repositories;

import com.example.cineverse.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByMovieId(int movieId);

    List<Review> findByUserId(int userId);

    boolean existsByUserIdAndMovieId(int userId, int movieId);

    int countByMovieId(int movieId);
}
