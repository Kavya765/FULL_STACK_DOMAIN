package com.example.cineverse.services;

import com.example.cineverse.models.Movie;
import com.example.cineverse.models.User;
import com.example.cineverse.models.Watchlist;
import com.example.cineverse.repositories.WatchlistRepository;
import com.example.cineverse.repositories.UserRepository;
import com.example.cineverse.repositories.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public WatchlistService(WatchlistRepository watchlistRepository,
                            UserRepository userRepository,
                            MovieRepository movieRepository) {
        this.watchlistRepository = watchlistRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    // Get watchlisted movies by user ID
    public List<Movie> getWatchlistedMovies(int userId) {
        List<Watchlist> watchlistEntries = watchlistRepository.findByUserId(userId);
        return watchlistEntries.stream()
                .map(Watchlist::getMovie)
                .collect(Collectors.toList());
    }

    // Check if a movie is in user's watchlist
    public boolean isMovieInWatchlist(int userId, int movieId) {
        return watchlistRepository.existsByUserIdAndMovieId(userId, movieId);
    }

    // Add or remove movie from watchlist
    public Watchlist addToWatchlist(int userId, int movieId) {
        Watchlist watchlist = new Watchlist();

        watchlist.setUser(userRepository.findById(userId));
        watchlist.setMovie(movieRepository.findById(movieId));

        return watchlistRepository.save(watchlist);
    }

    @Transactional
    public boolean removeWatchlist(int userId, int movieId) {
        return watchlistRepository.deleteByUserIdAndMovieId(userId, movieId) > 0;
    }
}
