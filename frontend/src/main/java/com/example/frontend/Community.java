package com.example.frontend;

import com.example.frontend.models.Movie;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

public class Community {
    private VBox root;
    private Movies movies;
    private ObservableList<String> movieList;

    public Community(Movies movies) {
        this.movies = movies;
        root = new VBox(20);

        Label title = new Label("Pick a movie to check out its community!");
        title.getStyleClass().add("forum-title");
        HBox titleContainer = new HBox(title);
        titleContainer.setAlignment(Pos.CENTER);
        titleContainer.setPadding(new Insets(150, 0, 50, 0)); // Adds top and bottom spacing

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search for a movie...");
        searchBar.getStyleClass().add("search-field");
        HBox searchBarContainer = new HBox(searchBar);
        searchBarContainer.setAlignment(Pos.CENTER);

        List<Movie> movieList = MoviesClient.getAllMovies();

        GridPane movieGrid = createGrid(movieList);
        movieGrid.setAlignment(Pos.CENTER);

        searchBar.setOnKeyTyped(event -> {
            String query = searchBar.getText().toLowerCase();
            List<Movie> filteredMovies = movieList.stream()
                    .filter(movie -> movie.getTitle().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());

            // Update the grid with the filtered movies
            movieGrid.getChildren().clear();  // Clear the current grid
            movieGrid.getChildren().add(createGrid(filteredMovies));
        });

        root.getChildren().addAll(titleContainer, searchBarContainer, movieGrid);
    }

    private GridPane createGrid(List<Movie> movies) {
        GridPane grid = new GridPane();
        int column = 0;
        int row = 0;

        for (Movie movie : movies) {
            ImageView moviePoster = new ImageView(new Image(getClass().getResource(movie.getPosterUrl()).toExternalForm()));
            moviePoster.setFitWidth(200);
            moviePoster.setFitHeight(280);
            moviePoster.getStyleClass().add("image-view");

            VBox movieBox = new VBox(moviePoster);
            movieBox.setAlignment(Pos.CENTER);

            movieBox.setOnMouseClicked(event -> openMovieForum(movie));

            grid.add(movieBox, column, row);

            column++;
            if (column > 3) {
                column = 0;
                row++;
            }
        }
        grid.getStyleClass().add("grid-pane");

        return grid;
    }

    public VBox getRoot() {
        return root;
    }

    private void openMovieForum(Movie movie) {
        movies.switchMovieForum(movie);
    }
}

