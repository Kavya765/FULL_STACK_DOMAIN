package com.example.cineverse.services;

import com.example.cineverse.models.Movie;
import com.example.cineverse.models.User;
import com.example.cineverse.models.Watched;
import com.example.cineverse.models.Watchlist;
import com.example.cineverse.repositories.WatchedRepository;
import com.example.cineverse.repositories.UserRepository;
import com.example.cineverse.repositories.MovieRepository;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WatchedService {

    private final WatchedRepository watchedRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public WatchedService(WatchedRepository watchedRepository, UserRepository userRepository, MovieRepository movieRepository) {
        this.watchedRepository = watchedRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    // Get list of watched movies by user ID
    public List<Movie> getWatchedMovies(int userId) {
        List<Watched> watchedList = watchedRepository.findByUserId(userId);
        return watchedList.stream()
                .map(Watched::getMovie)
                .collect(Collectors.toList());
    }

    // Check if a movie is watched by user
    public boolean isMovieWatched(int userId, int movieId) {
        return watchedRepository.existsByUserIdAndMovieId(userId, movieId);
    }

    // Add or remove watched status
    public Watched addToWatched(int userId, int movieId) {
        Watched watched = new Watched();

        watched.setUser(userRepository.findById(userId));
        watched.setMovie(movieRepository.findById(movieId));

        return watchedRepository.save(watched);
    }

    @Transactional
    public boolean removeWatched(int userId, int movieId) {
        return watchedRepository.deleteByUserIdAndMovieId(userId,movieId) > 0;
    }
}
