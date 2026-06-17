package com.example.cineverse.services;

import com.example.cineverse.models.Genre;
import com.example.cineverse.models.Movie;
import com.example.cineverse.models.MovieGenre;
import com.example.cineverse.repositories.GenreRepository;
import com.example.cineverse.repositories.MovieGenreRepository;
import com.example.cineverse.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieGenreService {

    private final MovieGenreRepository movieGenreRepository;
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;

    @Autowired
    public MovieGenreService(MovieGenreRepository movieGenreRepository,
                             MovieRepository movieRepository,
                             GenreRepository genreRepository) {
        this.movieGenreRepository = movieGenreRepository;
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
    }

    public List<MovieGenre> getMovieGenresByMovieId(int movieId) {
        return movieGenreRepository.findByMovieId(movieId);
    }

    public List<Genre> getGenresOfMovie(int movieId) {
        return getMovieGenresByMovieId(movieId)
                .stream()
                .map(MovieGenre::getGenre)
                .collect(Collectors.toList());
    }

    public boolean addGenreToMovie(int movieId, String genreName) {
        Genre genre = genreRepository.getGenreByName(genreName);
        if (!movieRepository.existsById(movieId) || !genreRepository.existsById(genre.getGenreId())) {
            return false;
        }
        if (movieGenreRepository.existsByMovieIdAndGenreId(movieId, genre.getGenreId())) {
            return false;
        }
        Movie movie = movieRepository.findById(movieId);

        MovieGenre movieGenre = new MovieGenre();
        movieGenre.setMovie(movie);
        movieGenre.setGenre(genre);
        movieGenreRepository.save(movieGenre);
        return true;
    }

    public void removeGenreFromMovie(int movieId, int genreId) {
        List<MovieGenre> movieGenres = movieGenreRepository.findByMovieId(movieId);
        movieGenres.stream()
                .filter(mg -> mg.getGenre().getGenreId() == genreId)
                .findFirst()
                .ifPresent(movieGenreRepository::delete);
    }
}
