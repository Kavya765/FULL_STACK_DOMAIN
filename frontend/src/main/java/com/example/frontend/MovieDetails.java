package com.example.frontend;


import com.example.frontend.models.Movie;
import com.example.frontend.models.Review;
import com.example.frontend.models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class MovieDetails {
    private int id;
    private VBox root;


    public MovieDetails(int id){
        this.id = id;
        Movie movie = MoviesClient.getMovieById(id);

        ImageView moviePoster = new ImageView(new Image(getClass().getResource(movie.getPosterUrl()).toExternalForm()));
        moviePoster.setFitWidth(300);
        moviePoster.setFitHeight(400);
        moviePoster.getStyleClass().add("movie-poster");
        HBox posterContainer = new HBox(moviePoster);
        posterContainer.setAlignment(Pos.CENTER);

        Label title = new Label(movie.getTitle());
        title.getStyleClass().add("movie-title");

        Label releaseDate = new Label(movie.getReleaseDate().toString());

        releaseDate.getStyleClass().add("movie-release-date");

        HBox titleReleaseDate = new HBox(title, releaseDate);

        Label director = new Label("Director: " + movie.getDirector());
        director.getStyleClass().add("movie-director");

        Label actors = new Label("Actors: " + movie.getActors().toString());
        actors.getStyleClass().add("movie-actors");

        HBox directorActors = new HBox(director, actors);

        Label rated = new Label(movie.getrated());
        rated.getStyleClass().add("movie-rated");

        List<String> genreList = MoviesClient.getMoviesGenre(id);


        String genresText = genreList != null ? String.join(", ", genreList) : "No genres available";


        Label genres = new Label(genresText);
        genres.getStyleClass().add("movie-genres");
        HBox ratedGenres = new HBox(rated, genres);

        boolean isFavorite = MovieDetailsClient.isMovieFavorited(UserSession.getInstance().getUserId(), id);
        boolean isWatchlisted = MovieDetailsClient.isMovieInWatchlist(UserSession.getInstance().getUserId(), id);
        boolean isWatched = MovieDetailsClient.isMovieWatched(UserSession.getInstance().getUserId(), id);

        ImageView favoriteIcon = new ImageView(new Image(getClass().getResource(isFavorite ? "/images/filledheart.png" : "/images/emptyheart.png").toExternalForm()));
        ImageView watchedIcon = new ImageView(new Image(getClass().getResource(isWatched ? "/images/watched.png" : "/images/notwatched.png").toExternalForm()));
        ImageView watchlistIcon = new ImageView(new Image(getClass().getResource(isWatchlisted ? "/images/filledwatchlist.png" : "/images/emptywatchlist.png").toExternalForm()));

        favoriteIcon.setFitHeight(40);
        favoriteIcon.setFitWidth(40);
        favoriteIcon.setPreserveRatio(true);

        watchedIcon.setFitHeight(40);
        watchedIcon.setFitWidth(40);
        watchedIcon.setPreserveRatio(true);

        watchlistIcon.setFitHeight(40);
        watchlistIcon.setFitWidth(40);
        watchlistIcon.setPreserveRatio(true);

        Button favorite = new Button();
        favorite.setGraphic(favoriteIcon);
        favorite.getStyleClass().add("movie-favorite");

        Button watched = new Button();
        watched.setGraphic(watchedIcon);
        watched.getStyleClass().add("movie-watched");

        Button watchlist = new Button();
        watchlist.setGraphic(watchlistIcon);
        watchlist.getStyleClass().add("movie-watchlist");

        favorite.setOnAction(e -> {
            boolean newStatus = !MovieDetailsClient.isMovieFavorited(UserSession.getInstance().getUserId(), id);
            if (MovieDetailsClient.updateFavoriteStatus(UserSession.getInstance().getUserId(), id, newStatus)) {
                favoriteIcon.setImage(new Image(getClass().getResource(newStatus ? "/images/filledheart.png" : "/images/emptyheart.png").toExternalForm()));
            }
        });

        // Enhanced debugging version with state verification

        watched.setOnAction(e -> {
            int userId = UserSession.getInstance().getUserId();

            if(MovieDetailsClient.isMovieWatched(userId, id)) {
                MovieDetailsClient.removeWatched(userId, id);
            } else if(MovieDetailsClient.isMovieInWatchlist(userId, id)) {
                MovieDetailsClient.removeWatchlist(userId, id);
                MovieDetailsClient.addToWatched(userId, id);
            } else {
                MovieDetailsClient.addToWatched(userId, id);
            }

            watchlistIcon.setImage(new Image(getClass().getResource(
                    MovieDetailsClient.isMovieInWatchlist(userId, id) ? "/images/filledwatchlist.png" : "/images/emptywatchlist.png"
            ).toExternalForm()));
            watchedIcon.setImage(new Image(getClass().getResource(
                    MovieDetailsClient.isMovieWatched(userId, id) ? "/images/watched.png" : "/images/notwatched.png"
            ).toExternalForm()));
        });

        watchlist.setOnAction(e -> {
            int userId = UserSession.getInstance().getUserId();

            if(MovieDetailsClient.isMovieInWatchlist(userId, id)) {
                MovieDetailsClient.removeWatchlist(userId, id);
            } else if(MovieDetailsClient.isMovieWatched(userId, id)) {
                MovieDetailsClient.removeWatched(userId, id);
                MovieDetailsClient.addToWatchlist(userId, id);
            } else {
                MovieDetailsClient.addToWatchlist(userId, id);
            }

            watchlistIcon.setImage(new Image(getClass().getResource(
                    MovieDetailsClient.isMovieInWatchlist(userId, id) ? "/images/filledwatchlist.png" : "/images/emptywatchlist.png"
            ).toExternalForm()));
            watchedIcon.setImage(new Image(getClass().getResource(
                    MovieDetailsClient.isMovieWatched(userId, id) ? "/images/watched.png" : "/images/notwatched.png"
            ).toExternalForm()));
        });

        HBox watchStatusHeader = new HBox(favorite, watched, watchlist);

        Label plot = new Label(movie.getPlot());
        plot.getStyleClass().add("movie-plot");

        List<Review> reviews = MovieDetailsClient.getReviewsByMovie(id);
        VBox reviewBox = new VBox(10);
        reviewBox.getStyleClass().add("review-box");
        reviewBox.setAlignment(Pos.TOP_LEFT);

        for (Review review : reviews) {
            VBox reviewItem = new VBox(5);
            reviewItem.getStyleClass().add("review-item");

            User user = MoviesClient.getUserById(review.getUserId());
            Label reviewerName = new Label(user.getUsername());
            reviewerName.getStyleClass().add("reviewer-name");

            Label reviewText = new Label(review.getReviewText());
            reviewText.getStyleClass().add("review-text");
            double rating = review.getRating();

            Image filledStar = new Image(getClass().getResource("/images/filledstar.png").toExternalForm());
            Image emptyStar = new Image(getClass().getResource("/images/emptystar.png").toExternalForm());

            HBox starsBox = new HBox(5);

            for (int i = 1; i <= 5; i++) {
                ImageView starView = new ImageView( i<=rating ? filledStar: emptyStar );
                starView.setFitHeight(15);
                starView.setFitWidth(15);
                starView.setPreserveRatio(true);

                // Add the star to the HBox
                starsBox.getChildren().add(starView);
            }

            starsBox.setAlignment(Pos.CENTER_LEFT);
            starsBox.setMaxWidth(Double.MAX_VALUE);

            HBox nameandrate = new HBox(5,reviewerName,starsBox);
            nameandrate.setAlignment(Pos.CENTER_LEFT);

            reviewItem.getChildren().addAll(nameandrate, reviewText);
            reviewBox.getChildren().add(reviewItem);
        }

        boolean hasReviewed = MovieDetailsClient.hasUserReviewedMovie(UserSession.getInstance().getUserId(), id);

        VBox reviewForm = new VBox(10);

        if (!hasReviewed) {
            Label reviewTitle = new Label("Write a Review:");
            reviewTitle.getStyleClass().add("review-title");
            TextArea reviewInput = new TextArea();
            reviewInput.setPromptText("Write your review here...");
            reviewInput.setPrefRowCount(3);
            reviewInput.getStyleClass().add("review-input");

            Label ratingLabel = new Label("Rating (1 to 5):");

            VBox ratingBox = new VBox(5);
            ratingBox.setAlignment(Pos.CENTER); // center the content horizontally
            ratingBox.setPadding(new Insets(10)); // optional padding

            HBox starsBox = new HBox(5);
            starsBox.setAlignment(Pos.CENTER); // center stars in the HBox

            int maxStars = 5;
            Image filledStar = new Image(getClass().getResource("/images/filledstar.png").toExternalForm());
            Image emptyStar = new Image(getClass().getResource("/images/emptystar.png").toExternalForm());

            double[] currentRating = {0};

            for (int i = 1; i <= maxStars; i++) {
                ImageView starView = new ImageView(emptyStar);
                starView.setFitHeight(24);
                starView.setFitWidth(24);
                starView.setPreserveRatio(true);

                StackPane starPane = new StackPane(starView);
                starPane.setPadding(new Insets(5));
                starPane.setStyle("-fx-cursor: hand;");

                int starIndex = i;
                starPane.setOnMouseClicked(e -> {
                    currentRating[0] = starIndex;
                    for (int j = 0; j < maxStars; j++) {
                        StackPane pane = (StackPane) starsBox.getChildren().get(j);
                        ImageView s = (ImageView) pane.getChildren().get(0);
                        s.setImage(j < starIndex ? filledStar : emptyStar);
                    }
                });

                starsBox.getChildren().add(starPane);
            }

            ratingBox.getChildren().addAll(ratingLabel, starsBox);

            Button submitReviewButton = new Button("Submit Review");
            submitReviewButton.getStyleClass().add("submit-review");

            submitReviewButton.setOnAction(event -> {
                String reviewText = reviewInput.getText().trim();
                double rating = currentRating[0];

                if (reviewText.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Please write a review before submitting.", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }

                if (rating == 0) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a rating before submitting.", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }

                boolean success = MovieDetailsClient.saveReview(id, reviewText, rating);
                if (success) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Review submitted successfully!", ButtonType.OK);
                    alert.showAndWait();
                    reviewInput.clear();

                    // Reset stars
                    for (Node node : starsBox.getChildren()) {
                        if (node instanceof ImageView) {
                            ((ImageView) node).setImage(emptyStar);
                        }
                    }
                    currentRating[0] = 0;

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to submit review. Try again later.", ButtonType.OK);
                    alert.showAndWait();
                }
            });
            reviewForm.setAlignment(Pos.CENTER);
            reviewForm.getChildren().addAll(reviewTitle, reviewInput, ratingBox, submitReviewButton);

        } else {
            Label alreadyReviewed = new Label("You have already reviewed this movie.");
            HBox alreadyReviewedBox = new HBox(alreadyReviewed);
            alreadyReviewedBox.setAlignment(Pos.CENTER);
            alreadyReviewedBox.getStyleClass().add("already-reviewed");

            reviewForm.getChildren().add(alreadyReviewedBox);
        }
        VBox centeredReviewSection = new VBox(20); // spacing between form and reviews
        centeredReviewSection.setAlignment(Pos.CENTER);
        centeredReviewSection.setPadding(new Insets(20)); // optional
        centeredReviewSection.getChildren().addAll(reviewForm, reviewBox);

        root = new VBox(
                posterContainer,
                titleReleaseDate,
                directorActors,
                ratedGenres,
                plot,
                watchStatusHeader,
                centeredReviewSection // replaces reviewForm and reviewBox
        );
        root.setSpacing(20); // optional for spacing

    }

    public VBox getRoot() {
        return root;
    }
}
