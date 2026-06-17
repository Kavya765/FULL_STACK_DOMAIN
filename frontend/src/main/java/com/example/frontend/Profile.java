package com.example.frontend;

import com.example.frontend.models.Movie;
import com.example.frontend.models.Review;
import com.example.frontend.models.User;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.List;

public class Profile {

    private int id;
    private VBox root;

    public Profile(int id){
        this.id = id;
        User user = MoviesClient.getUserById(id);


        Label username = new Label(user.getUsername());
        username.getStyleClass().add("profile-username");

        ImageView editIcon = new ImageView(new Image(getClass().getResource("/images/edit.png").toExternalForm()));
        editIcon.setFitHeight(20);
        editIcon.setFitWidth(20);
        editIcon.setPreserveRatio(true);

        Button editButton = new Button();
        editButton.setGraphic(editIcon);
        editButton.setStyle("-fx-background-color: transparent;");
        editButton.setOnAction(e -> {openEditUserForm();});
        editButton.setOnMouseEntered(event -> {editIcon.setImage(new Image(getClass().getResource("/images/editFilled.png").toExternalForm()));});
        editButton.setOnMouseExited(event -> {editIcon.setImage(new Image(getClass().getResource("/images/edit.png").toExternalForm()));});

        HBox usernameBox = new HBox(username,editButton);

        Label email = new Label("Email: " + user.getEmail());
        email.getStyleClass().add("profile-email");


        Label createdAt = new Label("Created on: " + user.getCreatedAt().toString());
        createdAt.getStyleClass().add("profile-created-at");

        root = new VBox(10, usernameBox, email, createdAt);
        root.getStyleClass().add("profile-section");

        Label watchedLabel = new Label("Watched");
        watchedLabel.getStyleClass().add("profile-watched");
        Line line1 = new Line(0, 0, 1000, 0);

        VBox watchedHeader = new VBox(10, watchedLabel, line1);

        GridPane watchedGrid = createGrid(ProfileClient.getWatchedMovies(user.getUserId()));

        VBox watchedSection = new VBox(10, watchedHeader, watchedGrid);

        Label watchlistLabel = new Label("Watchlist");
        watchlistLabel.getStyleClass().add("profile-watchlist");
        Line line2 = new Line(0, 0, 1000, 0);

        VBox watchlistHeader = new VBox(10, watchlistLabel, line2);

        GridPane watchlistGrid = createGrid(ProfileClient.getWatchlistMovies(user.getUserId()));

        VBox watchlistSection = new VBox(10, watchlistHeader, watchlistGrid);

        Label favoritesLabel = new Label("Favorites");
        favoritesLabel.getStyleClass().add("profile-favorites");
        Line line3 = new Line(0, 0, 1000, 0);

        VBox favoritesHeader = new VBox(10, favoritesLabel, line3);

        GridPane favoritesGrid = createGrid(ProfileClient.getFavoriteMovies(user.getUserId()));

        VBox favoritesSection = new VBox(10, favoritesHeader, favoritesGrid);

        Label yourReviewsLabel = new Label("Your Reviews");
        yourReviewsLabel.getStyleClass().add("profile-yourreviews");

        Line line4 = new Line(0, 0, 1000, 0);
        line4.setStroke(Color.BLACK);

        line1.getStyleClass().add("line");
        line2.getStyleClass().add("line");
        line3.getStyleClass().add("line");
        line4.getStyleClass().add("line");

        List<Review> reviews = ProfileClient.getUserReviews(id);



        VBox reviewBox = new VBox(10, yourReviewsLabel, line4);
        for (Review review : reviews) {
            VBox reviewItem = new VBox(5);
            reviewItem.getStyleClass().add("review-item");

            Movie movie = MoviesClient.getMovieById(review.getMovieId());

            Label movieName = new Label("Movie: " + movie.getTitle());
            Label reviewText = new Label(review.getReviewText());
            Label reviewRating = new Label("Rating: ");

            Image filledStar = new Image(getClass().getResource("/images/filledstar.png").toExternalForm());
            Image emptyStar = new Image(getClass().getResource("/images/emptystar.png").toExternalForm());


            HBox starsBox = new HBox(5);

            for (int i = 1; i <= 5; i++) {
                ImageView starView = new ImageView( i<=review.getRating() ? filledStar: emptyStar );
                starView.setFitHeight(15);
                starView.setFitWidth(15);
                starView.setPreserveRatio(true);

                starsBox.getChildren().add(starView);
            }

            reviewItem.getChildren().addAll(movieName, reviewText, reviewRating,starsBox);
            reviewBox.getChildren().add(reviewItem);
        }

        root.getChildren().addAll(watchedSection, watchlistSection, favoritesSection, reviewBox);
    }

    private void openEditUserForm() {
        Stage editStage = new Stage();
        editStage.setTitle("Edit User");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("New Password");

        Button updateButton = new Button("Update User");
        updateButton.setOnAction(e -> {
            int userId = UserSession.getInstance().getUserId();
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();

            User user = new User();
            user.setUserId(userId);
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);

            if(MoviesClient.updateUser(user)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "User updated successfully!", ButtonType.OK);
                alert.showAndWait();
                editStage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update user.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        layout.getChildren().addAll(usernameField, emailField, passwordField, updateButton);

        Scene scene = new Scene(layout, 400, 300);
        editStage.setScene(scene);
        editStage.show();
    }


    private GridPane createGrid(List<Movie> movies) {
        GridPane grid = new GridPane();
        int column = 0;
        int row = 0;

        for (Movie movie : movies) {
            ImageView moviePoster = new ImageView(new Image(getClass().getResource(movie.getPosterUrl()).toExternalForm()));

            moviePoster.setFitWidth(150);
            moviePoster.setFitHeight(200);
            moviePoster.getStyleClass().add("image-view");
            grid.add(moviePoster, column, row);

            column++;
            if (column > 5) {
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
}
