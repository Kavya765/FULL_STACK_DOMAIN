package com.example.cineverse.services;

import com.example.cineverse.models.Favorite;
import com.example.cineverse.models.Movie;
import com.example.cineverse.models.User;
import com.example.cineverse.repositories.FavoriteRepository;
import com.example.cineverse.repositories.MovieRepository;
import com.example.cineverse.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public FavoriteService(FavoriteRepository favoriteRepository, UserRepository userRepository, MovieRepository movieRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    // Get how many times a movie was favorited
    public int getFavoriteCount(int movieId) {
        return favoriteRepository.countByMovieId(movieId);
    }

    // Get all favorited movies for a user
    public List<Movie> getFavoritedMovies(int userId) {
        return favoriteRepository.findByUserId(userId)
                .stream()
                .map(Favorite::getMovie)
                .collect(Collectors.toList());
    }

    // Check if a movie is favorited by a user
    public boolean isMovieFavorited(int userId, int movieId) {
        return favoriteRepository.findByUserIdAndMovieId(userId, movieId).isPresent();
    }

    // Add or remove favorite status
    public boolean updateFavoriteStatus(int userId, int movieId, boolean add) {
        Optional<User> userOpt = Optional.ofNullable(userRepository.findById(userId));
        Optional<Movie> movieOpt = Optional.ofNullable(movieRepository.findById(movieId));

        if (userOpt.isEmpty() || movieOpt.isEmpty()) {
            return false;
        }

        if (add) {
            // Check if already favorited
            if (favoriteRepository.findByUserIdAndMovieId(userId, movieId).isPresent()) {
                return false;
            }
            Favorite favorite = new Favorite();
            favorite.setUser(userOpt.get());
            favorite.setMovie(movieOpt.get());
            favoriteRepository.save(favorite);
        } else {
            favoriteRepository.findByUserIdAndMovieId(userId, movieId)
                    .ifPresent(favoriteRepository::delete);
        }

        return true;
    }
}
