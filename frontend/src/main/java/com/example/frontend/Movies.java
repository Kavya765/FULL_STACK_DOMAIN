package com.example.frontend;

import com.example.frontend.models.Genre;
import com.example.frontend.models.Movie;
import com.example.frontend.models.User;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Movies extends Application {
    private Stage primaryStage;
    private VBox mainContainer;
    private TextField searchBar;
    private List<Movie> allMovies = MoviesClient.getAllMovies();
    private final Image popcornImage = new Image(getClass().getResource("/images/Popcorn3.png").toExternalForm());

    @Override
    public void start(Stage primaryStage) {
        Font.loadFont(getClass().getResourceAsStream("/fonts/Righteous-Regular.ttf"), 14);

        this.primaryStage = primaryStage;
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root-container");

        VBox header = createHeader();
        root.setTop(header);

        mainContainer = new VBox(10);
        mainContainer.getChildren().addAll(createBrowseBySection(), createMovieGrid(allMovies));
        mainContainer.getStyleClass().add("main-container");

        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        root.setCenter(scrollPane);

        Scene scene = new Scene(root, 1100, 700);
        scene.getStylesheets().add(getClass().getResource("/css/movies.css").toExternalForm());

        primaryStage.setTitle("Cineverse");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResource("/images/popcornicon.png").toExternalForm()));
        primaryStage.show();
    }

    private void switchProfile(){
       Profile profile = new Profile(UserSession.getInstance().getUserId());
       mainContainer.getChildren().clear();
        mainContainer.getChildren().add(profile.getRoot());
   }

    public void switchHome(){
        allMovies = MoviesClient.getAllMovies();
        mainContainer.getChildren().clear();
        mainContainer.getChildren().addAll(createBrowseBySection(), createMovieGrid(allMovies));
    }

   private void switchMovieDetails(int id){
     MovieDetails movieDetails = new MovieDetails(id);
      mainContainer.getChildren().clear();
       mainContainer.getChildren().addAll(movieDetails.getRoot());
  }

    private void switchForum(){
        Community community = new Community(this);
        mainContainer.getChildren().clear();
        mainContainer.getChildren().addAll(community.getRoot());
    }

    public void switchMovieForum(Movie movie){
        MovieCommunity forum = new MovieCommunity(this, movie);
        mainContainer.getChildren().clear();
        mainContainer.getChildren().add(forum.getRoot());
    }

    private void logout(){
        UserSession.clearSession();
        Login loginPage = new Login();
        Stage loginStage = new Stage();
        try {
            loginPage.start(loginStage);
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private VBox createHeader() {
        HBox header = new HBox(10);
        header.getStyleClass().add("header");
        header.setPadding(new Insets(5, 10, 5, 10));
        header.setAlignment(Pos.CENTER_LEFT); // Align items to the left

        Button homeButton = createMenuButton("", "https://img.icons8.com/ios-glyphs/30/ffffff/home.png");
        homeButton.setAlignment(Pos.CENTER_LEFT);
        homeButton.setOnAction(event -> switchHome());

        Button communityButton = new Button("Community");
        communityButton.setAlignment(Pos.CENTER_LEFT);
        communityButton.getStyleClass().add("community-button");
        communityButton.setOnAction(event -> switchForum());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search movies...");
        searchBar.getStyleClass().add("search-bar");
        searchBar.setMaxWidth(300);
        searchBar.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                updateMovieGrid(searchBar.getText());
            }
        });

        // Profile Button
        ImageView profileIcon = new ImageView(new Image(getClass().getResource("/images/profile.png").toExternalForm()));
        profileIcon.setFitWidth(30);
        profileIcon.setFitHeight(30);
        Button profileButton = new Button();
        profileButton.setGraphic(profileIcon);
        profileButton.setStyle("-fx-background-color: transparent;");
        profileButton.getStyleClass().add("profile-button");
        profileButton.setOnAction(event -> switchProfile());
        profileButton.setOnMouseEntered(event -> profileIcon.setImage(new Image(getClass().getResource("/images/profileHover.png").toExternalForm())));
        profileButton.setOnMouseExited(event -> profileIcon.setImage(new Image(getClass().getResource("/images/profile.png").toExternalForm())));

        // Logout Button
        ImageView logoutIcon = new ImageView(new Image(getClass().getResource("/images/logout.png").toExternalForm()));
        logoutIcon.setFitWidth(25);
        logoutIcon.setFitHeight(25);
        Button logoutButton = new Button();
        logoutButton.setGraphic(logoutIcon);
        logoutButton.setStyle("-fx-background-color: transparent;");
        logoutButton.setOnAction(event -> logout());
        logoutButton.setOnMouseEntered(event -> logoutIcon.setImage(new Image(getClass().getResource("/images/logoutHover.png").toExternalForm())));
        logoutButton.setOnMouseExited(event -> logoutIcon.setImage(new Image(getClass().getResource("/images/logout.png").toExternalForm())));

        header.getChildren().addAll(homeButton,communityButton, spacer, searchBar, profileButton, logoutButton);

        VBox fullHeader = new VBox();

        // If the user is an admin, add the admin menu bar
        if (MoviesClient.getUserById(UserSession.getInstance().getUserId()).getRole().equals("admin")) {
            Image normalImage = new Image(getClass().getResource("/images/admin.png").toExternalForm());
            Image hoverImage = new Image(getClass().getResource("/images/adminFilled.png").toExternalForm()); // The filled versio

            ImageView menuIcon = new ImageView(normalImage); // Path to menu icon
            menuIcon.setFitWidth(40);
            menuIcon.setFitHeight(40);

            Button menuButton = new Button();
            menuButton.setGraphic(menuIcon);
            menuButton.setStyle("-fx-background-color: transparent;");

            menuButton.setOnMouseEntered(e -> menuIcon.setImage(hoverImage));
            menuButton.setOnMouseExited(e -> menuIcon.setImage(normalImage));

            ContextMenu adminMenu = new ContextMenu();

            // Movies Submenu
            Menu moviesMenu = new Menu("Movies");
            MenuItem insertMovieItem = new MenuItem("Insert Movie");
            insertMovieItem.setOnAction(e -> openInsertMovieForm());
            MenuItem editMovieItem = new MenuItem("Edit Movie");
            editMovieItem.setOnAction(e -> openEditMovieForm());
            MenuItem addGenreItem = new MenuItem("Add Genre to Movie");
            addGenreItem.setOnAction(e -> openAddGenreForm());
            MenuItem deleteMovieItem = new MenuItem("Delete Movie");
            deleteMovieItem.setOnAction(e -> openDeleteMovieForm());
            moviesMenu.getItems().addAll(insertMovieItem, editMovieItem, addGenreItem,deleteMovieItem);

            // Users Submenu
            Menu usersMenu = new Menu("Users");
            MenuItem editUser = new MenuItem("Edit a User info");
            editUser.setOnAction(e -> openEditUserForm());
            MenuItem deleteUser = new MenuItem("Delete a User");
            deleteUser.setOnAction(e -> openDeleteUserForm());
            usersMenu.getItems().addAll(editUser, deleteUser);

//            // Reports Submenu
//            Menu reportsMenu = new Menu("Reports");
//            MenuItem reportItem = new MenuItem("View Reports");
//            reportItem.setOnAction(e -> openReportView());
//            reportsMenu.getItems().add(reportItem);

            adminMenu.getItems().addAll(moviesMenu, usersMenu);

            // Show menu on image click
            menuButton.setOnAction(e -> adminMenu.show(menuButton, Side.BOTTOM, 0, 0));

            // Wrap ImageView inside an HBox
            HBox menuContainer = new HBox(menuButton);
            menuContainer.getStyleClass().add("menu-container");
            menuContainer.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(menuContainer, Priority.NEVER);
            HBox.setMargin(menuIcon, new Insets(20, 0, 0, 0));

            fullHeader.getChildren().addAll(menuContainer, header);
        } else {
            fullHeader.getChildren().add(header);
        }
        return fullHeader;
    }

//    private void openReportView() {
//        Stage reportStage = new Stage();
//        VBox reportLayout = new VBox(10);
//        reportLayout.setStyle("-fx-padding: 10;");
//
//        // Create a TableView to display the movie report
//        TableView<MovieReport> reportTable = new TableView<>();
//        reportTable.setPrefWidth(600);
//
//        // Define the columns for the report
//        TableColumn<MovieReport, String> titleColumn = new TableColumn<>("Movie Title");
//        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
//
//        TableColumn<MovieReport, Integer> watchlistColumn = new TableColumn<>("Watchlist Count");
//        watchlistColumn.setCellValueFactory(new PropertyValueFactory<>("watchlistCount"));
//
//        TableColumn<MovieReport, Integer> favoriteColumn = new TableColumn<>("Favorite Count");
//        favoriteColumn.setCellValueFactory(new PropertyValueFactory<>("favoriteCount"));
//
//        TableColumn<MovieReport, Double> avgRatingColumn = new TableColumn<>("Avg Rating");
//        avgRatingColumn.setCellValueFactory(new PropertyValueFactory<>("averageRating"));
//
//        TableColumn<MovieReport, Integer> reviewCountColumn = new TableColumn<>("Review Count");
//        reviewCountColumn.setCellValueFactory(new PropertyValueFactory<>("reviewCount"));
//
//        // Add columns to the TableView
//        reportTable.getColumns().addAll(titleColumn, watchlistColumn, favoriteColumn, avgRatingColumn, reviewCountColumn);
//
//        // Fetch the report data
//        List<MovieReport> reportData = db.getAllMovieReports();
//        System.out.println("Fetched report data: " + reportData.size());  // Debugging line to print number of records
//
//        ObservableList<MovieReport> reportList = FXCollections.observableArrayList(reportData);
//        reportTable.setItems(reportList);
//
//        // Add the TableView to the report layout
//        reportLayout.getChildren().add(reportTable);
//
//        Scene reportScene = new Scene(reportLayout, 800, 600);
//        reportStage.setTitle("Movie Reports");
//        reportStage.setScene(reportScene);
//        reportStage.show();
//    }

    private void openInsertMovieForm() {
        Stage insertMovieStage = new Stage();
        VBox formLayout = new VBox(10);
        formLayout.setStyle("-fx-padding: 10;");

        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        DatePicker releaseDatePicker = new DatePicker();

        TextField posterUrlField = new TextField();
        posterUrlField.setPromptText("Poster URL (Auto-filled after upload)");
        posterUrlField.setEditable(false);

        TextField ratedField = new TextField();
        ratedField.setPromptText("Rated");

        TextField directorField = new TextField();
        directorField.setPromptText("Director");

        TextField actorsField = new TextField();
        actorsField.setPromptText("Actors");

        TextArea plotArea = new TextArea();
        plotArea.setPromptText("Plot");

        Button uploadButton = new Button("Upload Image");
        uploadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(insertMovieStage);

            if (selectedFile != null) {
                try {
                    String originalFileName = selectedFile.getName();

                    File targetDirectory = new File("src/main/resources/images");
                    if (!targetDirectory.exists()) {
                        targetDirectory.mkdirs();
                    }

                    File destinationFile = new File(targetDirectory, originalFileName);

                    Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    posterUrlField.setText("/images/" + originalFileName);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to upload image.", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        });

        Button submitButton = new Button("Insert Movie");
        submitButton.setOnAction(e -> {
            Movie movie = new Movie();
            movie.setTitle(titleField.getText());
            movie.setReleaseDate(releaseDatePicker.getValue());
            movie.setPosterUrl(posterUrlField.getText());
            movie.setrated(ratedField.getText());
            movie.setDirector(directorField.getText());
            movie.setActors(actorsField.getText());
            movie.setPlot(plotArea.getText());

            MoviesClient.insertMovie(movie);
            insertMovieStage.close();
        });


        formLayout.getChildren().addAll(
                titleField, releaseDatePicker, uploadButton,
                ratedField, directorField, actorsField, plotArea, submitButton
        );

        Scene formScene = new Scene(formLayout, 350, 450);
        insertMovieStage.setScene(formScene);
        insertMovieStage.setTitle("Insert Movie");
        insertMovieStage.show();
    }

    private void openEditMovieForm() {
        Stage editStage = new Stage();
        editStage.setTitle("Edit Movie");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TextField movieIdField = new TextField();
        movieIdField.setPromptText("Enter Movie ID");

        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextField releaseDateField = new TextField();
        releaseDateField.setPromptText("Release Date");

        TextField posterUrlField = new TextField();
        posterUrlField.setPromptText("Poster URL");
        posterUrlField.setEditable(false);  // Disable editing the poster URL directly

        Button uploadButton = new Button("Upload Poster Image");
        uploadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(editStage);

            if (selectedFile != null) {
                try {
                    String originalFileName = selectedFile.getName();

                    File targetDirectory = new File("src/main/resources/images");
                    if (!targetDirectory.exists()) {
                        targetDirectory.mkdirs();
                    }

                    File destinationFile = new File(targetDirectory, originalFileName);

                    Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                    posterUrlField.setText("/images/" + originalFileName);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to upload image.", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        });

        TextField ratedField = new TextField();
        ratedField.setPromptText("Rated");

        TextField directorField = new TextField();
        directorField.setPromptText("Director");

        TextField actorsField = new TextField();
        actorsField.setPromptText("Actors");

        TextArea plotField = new TextArea();
        plotField.setPromptText("Plot");

        Button updateButton = new Button("Update Movie");
        updateButton.setOnAction(e -> {
            try {
                int movieId = Integer.parseInt(movieIdField.getText());
                String title = titleField.getText();
                String releaseDateInput = releaseDateField.getText().trim();
                String posterUrl = posterUrlField.getText();
                String rated = ratedField.getText();
                String director = directorField.getText();
                String actors = actorsField.getText();
                String plot = plotField.getText();

                LocalDate localDate = null;
                if (!releaseDateInput.isEmpty()) {
                    // Parse the date from M/d/yyyy format (like 5/6/2025)
                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                    localDate = LocalDate.parse(releaseDateInput, inputFormatter);
                }

                Movie movie = new Movie();
                movie.setId(movieId);
                movie.setTitle(title);
                movie.setReleaseDate(localDate);
                movie.setPosterUrl(posterUrl);
                movie.setrated(rated);
                movie.setDirector(director);
                movie.setActors(actors);
                movie.setPlot(plot);

                if (MoviesClient.updateMovie(movie)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Movie updated successfully!", ButtonType.OK);
                    alert.showAndWait();
                    editStage.close();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update movie.", ButtonType.OK);
                    alert.showAndWait();
                }

            } catch (DateTimeParseException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Invalid date format. Please enter date as M/D/YYYY (e.g., 5/6/2025)",
                        ButtonType.OK);
                alert.showAndWait();
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Invalid movie ID. Please enter a valid number.",
                        ButtonType.OK);
                alert.showAndWait();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "An error occurred: " + ex.getMessage(),
                        ButtonType.OK);
                alert.showAndWait();
            }
        });

        layout.getChildren().addAll(movieIdField, titleField, releaseDateField, uploadButton, ratedField,
                directorField, actorsField, plotField, updateButton);

        Scene scene = new Scene(layout, 400, 500);
        editStage.setScene(scene);
        editStage.show();
    }

    private void openAddGenreForm() {
        Stage genreStage = new Stage();
        genreStage.setTitle("Add Genre to Movie");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label movieIdLabel = new Label("Enter Movie ID:");

        // TextField for entering Movie ID
        TextField movieIdField = new TextField();
        movieIdField.setPromptText("Enter Movie ID");

        Label genreLabel = new Label("Select Genre:");

        // ComboBox for selecting a genre
        ComboBox<Genre> genreDropdown = new ComboBox<>();
        genreDropdown.setPromptText("Select Genre");

        // Fetch genres from the database
        List<Genre> genres = MoviesClient.getGenres();
        genreDropdown.getItems().addAll(genres);


        Button addButton = new Button("Add Genre");
        addButton.setOnAction(e -> {
            String movieIdText = movieIdField.getText();
            Genre selectedGenre = genreDropdown.getValue();

            if (movieIdText.isEmpty() || selectedGenre == null) {
                new Alert(Alert.AlertType.WARNING, "Please enter a movie ID and select a genre.", ButtonType.OK).showAndWait();
                return;
            }

            int movieId;
            try {
                movieId = Integer.parseInt(movieIdText);
            } catch (NumberFormatException ex) {
                new Alert(Alert.AlertType.ERROR, "Invalid Movie ID format.", ButtonType.OK).showAndWait();
                return;
            }

            if (MoviesClient.createMovieGenre(movieId, String.valueOf(selectedGenre))) {
                new Alert(Alert.AlertType.INFORMATION, "Genre added successfully!", ButtonType.OK).showAndWait();
                genreStage.close();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to add genre.", ButtonType.OK).showAndWait();
            }
        });

        layout.getChildren().addAll(movieIdLabel, movieIdField, genreLabel, genreDropdown, addButton);

        Scene scene = new Scene(layout, 400, 250);
        genreStage.setScene(scene);
        genreStage.show();
    }



    private void openDeleteMovieForm() {
        Stage deleteStage = new Stage();
        deleteStage.setTitle("Delete Movie");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Enter movie title to delete:");
        TextField movieTitleField = new TextField();
        movieTitleField.setPromptText("Movie Title");

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            String movieTitle = movieTitleField.getText().trim();
            if (!movieTitle.isEmpty()) {
                boolean success = MoviesClient.deleteMovieByTitle(movieTitle); // Assumes a method to delete by title
                Alert alert;
                if (success) {
                    alert = new Alert(Alert.AlertType.INFORMATION, "Movie deleted successfully.", ButtonType.OK);
                    movieTitleField.clear();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR, "Movie not found or could not be deleted.", ButtonType.OK);
                }
                alert.showAndWait();
            } else {
                new Alert(Alert.AlertType.WARNING, "Please enter a movie title.", ButtonType.OK).showAndWait();
            }
        });

        layout.getChildren().addAll(titleLabel, movieTitleField, deleteButton);

        Scene scene = new Scene(layout, 300, 200);
        deleteStage.setScene(scene);
        deleteStage.show();
    }

    private void openEditUserForm() {
        Stage editStage = new Stage();
        editStage.setTitle("Edit User");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label userIdLabel = new Label("Enter user ID to edit:");
        TextField userIdField = new TextField();
        userIdField.setPromptText("ID");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("New Password");

        // Dropdown for user role
        ComboBox<String> roleDropdown = new ComboBox<>();
        roleDropdown.getItems().addAll("user", "admin");
        roleDropdown.setPromptText("Select Role");

        Button updateButton = new Button("Update User");
        updateButton.setOnAction(e -> {
            int userId = Integer.parseInt(userIdField.getText().trim());
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String role = roleDropdown.getValue();

            User user = new User();
            user.setUserId(userId);
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole(role);

            if (MoviesClient.updateUser(user)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "User updated successfully!", ButtonType.OK);
                alert.showAndWait();
                editStage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update user.", ButtonType.OK);
                alert.showAndWait();
            }
        });

        layout.getChildren().addAll(userIdLabel,userIdField,usernameField, emailField, passwordField, roleDropdown, updateButton);

        Scene scene = new Scene(layout, 400, 350);
        editStage.setScene(scene);
        editStage.show();
    }
    private void openDeleteUserForm() {
        Stage deleteStage = new Stage();
        deleteStage.setTitle("Delete User");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label instructionLabel = new Label("Enter username to delete a user:");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username to delete");

        Button deleteButton = new Button("Delete User");
        deleteButton.setOnAction(e -> {
            String selectedUser = usernameField.getText().trim();

            if (selectedUser == null || selectedUser.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please select or enter a username.", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            // Confirmation Alert
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete user: " + selectedUser + "?",
                    ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.YES) {
                if (MoviesClient.deleteUserByUsername(selectedUser)) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "User deleted successfully!", ButtonType.OK);
                    successAlert.showAndWait();
                    deleteStage.close();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Failed to delete user.", ButtonType.OK);
                    errorAlert.showAndWait();
                }
            }
        });

        layout.getChildren().addAll(instructionLabel, usernameField, deleteButton);

        Scene scene = new Scene(layout, 400, 250);
        deleteStage.setScene(scene);
        deleteStage.show();
    }

    private VBox createBrowseBySection() {
        VBox container = new VBox();

        container.setAlignment(Pos.TOP_CENTER);
        container.setPadding(new Insets(0, 0, 0, 0));

        Text appTitle = new Text("Cineverse");
        appTitle.getStyleClass().add("cineverse-title");

        // Create a Pane for the popcorn
        Pane popcornPane = new Pane();
        popcornPane.setPrefSize(500, 400);

        // StackPane to overlay the popcorn and title
        StackPane titleWithPopcorn = new StackPane(popcornPane, appTitle);
        titleWithPopcorn.setAlignment(Pos.TOP_CENTER); // Ensure elements are centered
        StackPane.setMargin(appTitle, new Insets(70, 0, 0, 0)); // Adjust title positioning if needed

        container.getChildren().add(titleWithPopcorn);

        // Wait until scene is set, then get the center position
        container.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                popcornPane.prefWidthProperty().bind(newScene.widthProperty());

                // Start the periodic creation of popcorn
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), e -> {

                    double sceneWidth = newScene.getWidth();
                    double sceneHeight = newScene.getHeight();
                    createAndAnimatePopcorn(popcornPane, sceneWidth, sceneHeight);
                }));

                timeline.setCycleCount(Timeline.INDEFINITE); // Repeat
                timeline.play(); // Start the periodic popcorn creation
            }
        });

        // Space between title & browse section
        Region spaceBetween = new Region();
        VBox.setVgrow(spaceBetween, Priority.ALWAYS);
        container.getChildren().add(spaceBetween);

        // Browse by section
        HBox browseBySection = new HBox(10);
        browseBySection.setAlignment(Pos.CENTER_LEFT);
        Text browseByLabel = createStyledText("Browse by:", 18, Color.WHITE);

        // Create filter dropdowns
        List<Genre> genres = MoviesClient.getGenres();
        List<String> genreNames = new ArrayList<>();
        for(Genre genre: genres){
            genreNames.add(genre.getName());
        }
        ComboBox<String> browseByGenre = createStyledComboBox(genreNames, "GENRE");
        ComboBox<String> browseByYear = createStyledComboBox(List.of( "2025", "2024", "2023", "2022", "2021", "2020", "2019", "2018", "2017", "2016",
                "2015", "2014", "2013", "2012", "2011", "2010", "2009", "2008", "2007", "2006",
                "2005", "2004", "2003", "2002", "2001", "2000", "1999", "1998", "1997", "1996",
                "1995", "1994", "1993", "1992", "1991", "1990", "1989", "1988", "1987", "1986",
                "1985", "1984", "1983", "1982", "1981", "1980"), "YEAR");
        ComboBox<String> browseByRating = createStyledComboBox(List.of("PG-13", "R", "PG","G","NC 17"), "RATING");

        ImageView resetImageView = new ImageView(new Image(getClass().getResource("/images/reset.png").toExternalForm()));
        resetImageView.setFitWidth(30);
        resetImageView.setFitHeight(30);
        resetImageView.setPreserveRatio(true);

        Button resetButton = new Button();
        resetButton.setGraphic(resetImageView);
        resetButton.getStyleClass().add("reset-button");
        resetButton.setOnAction(e -> resetFilters());

        browseBySection.getChildren().addAll(browseByLabel, browseByGenre, browseByYear, browseByRating, resetButton);
        container.getChildren().add(browseBySection);

        // Event Listeners: Update movie grid when a filter is selected
        browseByGenre.setOnAction(event -> updateFilteredMovies(browseByGenre, browseByYear, browseByRating));
        browseByYear.setOnAction(event -> updateFilteredMovies(browseByGenre, browseByYear, browseByRating));
        browseByRating.setOnAction(event -> updateFilteredMovies(browseByGenre, browseByYear, browseByRating));

        // ADD TOGGLE SWITCH FOR GRID/LIST VIEW
        HBox viewToggleBox = new HBox(10);
        viewToggleBox.setAlignment(Pos.CENTER_LEFT);

        ToggleGroup viewToggleGroup = new ToggleGroup();

        RadioButton gridViewButton = new RadioButton("📸 Grid View");
        gridViewButton.setToggleGroup(viewToggleGroup);
        gridViewButton.setSelected(true); // Default view
        gridViewButton.getStyleClass().add("radio-button");
        VBox.setMargin(viewToggleBox, new Insets(20, 0, 0, 0)); // 20px top margin

        RadioButton listViewButton = new RadioButton("📃 List View");
        listViewButton.setToggleGroup(viewToggleGroup);
        listViewButton.getStyleClass().add("radio-button");

        viewToggleBox.getChildren().addAll(gridViewButton, listViewButton);

        //  EVENT HANDLING FOR TOGGLE SWITCH
        gridViewButton.setOnAction(event -> updateView(true));  // Grid view
        listViewButton.setOnAction(event -> updateView(false)); // List view

        container.getChildren().clear();
        container.getChildren().addAll(titleWithPopcorn, browseBySection, viewToggleBox);

        return container;
    }
    private void updateView(boolean isGridView) {
        // Identify and remove the movie grid or list while keeping the browse section
        mainContainer.getChildren().remove(1);

        if (isGridView) {
            mainContainer.getChildren().add(createMovieGrid(allMovies));
        } else {
            mainContainer.getChildren().add(createMovieList(allMovies));
        }
    }

    private void resetFilters(){
        allMovies = MoviesClient.getAllMovies();
        mainContainer.getChildren().clear();
        mainContainer.getChildren().addAll(createBrowseBySection(),createMovieGrid(allMovies));
    }

    private VBox createMovieList(List<Movie> movies) {
        VBox listView = new VBox(10);
        listView.setPadding(new Insets(20, 0, 40, 0));

        for (Movie movie : movies) {
            // Main container
            HBox movieItem = new HBox(15);
            movieItem.setAlignment(Pos.CENTER);
            movieItem.setMinHeight(130);
            movieItem.setMaxHeight(130);
            movieItem.setStyle("-fx-background-color: transparent;");

            // Clickable content container
            HBox clickableContent = new HBox(15);
            clickableContent.setAlignment(Pos.CENTER);

            // Movie Poster
            ImageView moviePoster = new ImageView(new Image(getClass().getResource(movie.getPosterUrl()).toExternalForm()));
            moviePoster.setFitWidth(80);
            moviePoster.setFitHeight(120);
            moviePoster.setPreserveRatio(true);

            // Movie Details
            VBox details = new VBox(5);
            details.setAlignment(Pos.CENTER_LEFT);
            details.setMinWidth(200);
            details.setMaxWidth(200);

            Label title = new Label(movie.getTitle());
            title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            Label genre = new Label("Genre: " + MoviesClient.getMoviesGenre(movie.getId()));
            Label year = new Label("Year: " + (movie.getReleaseDate() != null ?
                    movie.getReleaseDate().toString().substring(0, 4) : "Unknown"));

            details.getChildren().addAll(title, genre, year);
            clickableContent.getChildren().addAll(moviePoster, details);

            // Main click handler (now on the content, not the container)
            clickableContent.setOnMouseClicked(event -> {

                switchMovieDetails(movie.getId());
            });

            movieItem.getChildren().add(clickableContent);

            // Admin Trash Can (separate from clickable content)
            if (MoviesClient.getUserById(UserSession.getInstance().getUserId()).getRole().equals("admin")) {
                ImageView trashIcon = new ImageView(new Image("https://img.icons8.com/ios-glyphs/30/fa314a/trash.png"));
                trashIcon.setFitWidth(24);
                trashIcon.setFitHeight(24);
                trashIcon.setOpacity(0);
                trashIcon.setPickOnBounds(true);

                // Prevent event propagation to parent
                trashIcon.setOnMouseClicked(event -> {
                    event.consume();
                    deleteMovie(movie.getId());
                });

                // Hover effects
                movieItem.setOnMouseEntered(e -> trashIcon.setOpacity(1));
                movieItem.setOnMouseExited(e -> trashIcon.setOpacity(0));

                movieItem.getChildren().add(trashIcon);
            }

            listView.getChildren().add(movieItem);
        }
        return listView;
    }

    // Update method to support multiple filters at once
    private void updateFilteredMovies(ComboBox<String> genreComboBox, ComboBox<String> yearComboBox, ComboBox<String> ratingComboBox) {
        String selectedGenre = genreComboBox.getValue();
        String selectedYear = yearComboBox.getValue();
        String selectedRating = ratingComboBox.getValue();

        // Start with all movies
        List<Movie> filteredMovies = MoviesClient.getAllMovies();

        // Apply genre filter
        if (selectedGenre != null && !selectedGenre.isEmpty()) {
            filteredMovies = filteredMovies.stream()
                    .filter(movie -> MoviesClient.getMoviesGenre(movie.getId()).contains(selectedGenre)) // Filter by genre
                    .collect(Collectors.toList());
        }

        // Apply year filter
        if (selectedYear != null && !selectedYear.isEmpty()) {
            try {
                int year = Integer.parseInt(selectedYear);
                filteredMovies = filteredMovies.stream()
                        .filter(movie -> Integer.parseInt(movie.getReleaseDate().toString().substring(0,4)) == year) // Filter by year
                        .collect(Collectors.toList());
            } catch (NumberFormatException e) {
                System.out.println("Invalid year format: " + selectedYear);
            }
        }

        // Apply rating filter
        if (selectedRating != null && !selectedRating.isEmpty()) {
            filteredMovies = filteredMovies.stream()
                    .filter(movie -> movie.getrated().equalsIgnoreCase(selectedRating)) // Filter by rating
                    .collect(Collectors.toList());
        }

        // Clear existing content
        mainContainer.getChildren().remove(1);
        allMovies = filteredMovies;

        if (filteredMovies.isEmpty()) {
            HBox noMoviesBox = new HBox(new Label("No movies found for the selected filters."));
            noMoviesBox.setPrefHeight(250);
            noMoviesBox.getStyleClass().add("no-movies");
            mainContainer.getChildren().add(noMoviesBox);
        } else {
            mainContainer.getChildren().add(createMovieGrid(filteredMovies));  // Update with filtered movies
        }
    }


    private void updateMovieGrid(String searchQuery) {
        allMovies = MoviesClient.getAllMovies();

        List<Movie> filteredMovies = allMovies.stream()
                .filter(movie -> movie.getTitle().toLowerCase().contains(searchQuery.toLowerCase()))
                .collect(Collectors.toList());

        mainContainer.getChildren().clear();

        if(filteredMovies.isEmpty()){
            HBox noMoviesBox = new HBox(new Label("No movies were found."));
            noMoviesBox.setPrefHeight(250);
            noMoviesBox.getStyleClass().add("no-movies");
            mainContainer.getChildren().addAll(createBrowseBySection(), noMoviesBox);
        } else {
            mainContainer.getChildren().addAll(createBrowseBySection(), createMovieGrid(filteredMovies));
        }
        allMovies = filteredMovies;
    }

    // Method to create and animate a single popcorn kernel
    private void createAndAnimatePopcorn(Pane popcornPane, double sceneWidth, double sceneHeight) {
        ImageView kernel = new ImageView(popcornImage);
        kernel.setFitWidth(30);
        kernel.setFitHeight(30);

        // Random starting position for the kernels at the top
        double startX = Math.random() * sceneWidth; // Random X position across the scene width
        double startY = -30; // Start just above the scene (so they fall from the top)

        kernel.setX(startX);
        kernel.setY(startY);

        popcornPane.getChildren().add(kernel);

        // Random speed to make kernels fall at different speeds
        double randomSpeed = 2 + Math.random() * 3; // Random speed between 2 and 5 seconds

        // Animate popcorn falling downwards like rain with random speeds
        TranslateTransition transition = new TranslateTransition(Duration.seconds(randomSpeed), kernel);
        transition.setToY(sceneHeight); // Move to the bottom of the scene

        // Set action when animation is finished (to remove the popcorn once it reaches the bottom)
        transition.setOnFinished(event -> {
            popcornPane.getChildren().remove(kernel); // Remove the kernel once it reaches the bottom
        });

        transition.setInterpolator(Interpolator.LINEAR);
        transition.play();
    }


    // Custom method to create a styled ComboBox
    private ComboBox<String> createStyledComboBox(List<String> items, String promptText) {
        ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(items));
        comboBox.setPromptText(promptText);
        comboBox.getStyleClass().add("combo-box"); // Apply CSS class
        comboBox.setPrefWidth(100);
        comboBox.setPrefHeight(3);
        return comboBox;
    }

    private GridPane createMovieGrid(List<Movie> movies) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 0, 40, 0));
        grid.setAlignment(Pos.CENTER);

        int row = 0;
        int column = 0;
        for(Movie movie : movies){
            ImageView moviePoster = createMoviePoster(movie.getPosterUrl());
            moviePoster.getStyleClass().add("image-view");

            Label titleLabel = new Label(movie.getTitle());
            titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-alignment: center;");
            titleLabel.setWrapText(true);

            VBox movieBox = new VBox(moviePoster);
            movieBox.setAlignment(Pos.CENTER);

            movieBox.setOnMouseClicked(event -> switchMovieDetails(movie.getId()));
            // Admin Trash Can Feature
            if (MoviesClient.getUserById(UserSession.getInstance().getUserId()).getRole().equals("admin")) {
                ImageView trashIcon = new ImageView(new Image("https://img.icons8.com/ios-glyphs/30/fa314a/trash.png"));
                trashIcon.setFitWidth(24);
                trashIcon.setFitHeight(24);
                trashIcon.setOpacity(0); // Hidden by default

                trashIcon.setOnMouseClicked(event -> deleteMovie(movie.getId()));

                // Show trash can only on hover
                movieBox.setOnMouseEntered(e -> trashIcon.setOpacity(1));
                movieBox.setOnMouseExited(e -> trashIcon.setOpacity(0));

                movieBox.getChildren().add(trashIcon);
            }

            grid.add(movieBox, column, row);

            column++;
            if (column > 3) {
                column = 0;
                row++;
            }
        }

        return grid;
    }

    private void deleteMovie(int movieId) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this movie?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            if (MoviesClient.deleteMovieById(movieId)) {
                Alert success = new Alert(Alert.AlertType.INFORMATION, "Movie deleted successfully!", ButtonType.OK);
                success.showAndWait();
                List<Movie> updatedMovies = MoviesClient.getAllMovies(); // Fetch the latest movies
                GridPane newMovieList = createMovieGrid(updatedMovies); // Rebuild the list
                mainContainer.getChildren().remove(1);// Refresh the list after deletion
                mainContainer.getChildren().add(newMovieList);
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR, "Failed to delete movie!", ButtonType.OK);
                error.showAndWait();
            }
        }
    }

    private ImageView createMoviePoster(String imageUrl) {
        if (imageUrl == null) {
            System.err.println("Error: imageUrl is null.");
            return new ImageView(); // or return a placeholder image
        }

        URL resource = getClass().getResource(imageUrl);
        if (resource == null) {
            System.err.println("Error: Image not found at path: " + imageUrl);
            return new ImageView(); // or return a placeholder image
        }

        ImageView imageView = new ImageView(new Image(resource.toExternalForm()));
        imageView.setFitWidth(200);
        imageView.setFitHeight(280);
        return imageView;
    }

    private Button createMenuButton(String text, String iconUrl) {
        ImageView icon = new ImageView(new Image(iconUrl));
        Button button = new Button(text, icon);
        button.setStyle("-fx-background-color: transparent; -fx-padding: 5px; -fx-text-fill: white;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #444; -fx-padding: 5px; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: transparent; -fx-padding: 5px; -fx-text-fill: white;"));
        return button;
    }

    private ComboBox<String> createComboBox(List<String> items, String prompt) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(items);
        comboBox.setPromptText(prompt);
        comboBox.setStyle("-fx-padding: 8px; -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-border-color: #ffffff; -fx-text-fill: white;");
        return comboBox;
    }

    private Text createStyledText(String content, int fontSize, Color color) {
        Text text = new Text(content);
        text.setFont(Font.font("Poppins", FontWeight.BOLD, fontSize));
        text.setFill(color);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.rgb(50, 50, 50, 0.8));
        dropShadow.setRadius(10);
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);
        text.setEffect(dropShadow);

        return text;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

