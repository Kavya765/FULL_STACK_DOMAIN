package com.example.cineverse.repositories;

import com.example.cineverse.models.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieGenreRepository extends JpaRepository<MovieGenre, Integer> {

    List<MovieGenre> findByMovieId(int movieId);

    List<MovieGenre> findByGenreId(int genreId);

    boolean existsByMovieIdAndGenreId(int movieId, int genreId);
}
