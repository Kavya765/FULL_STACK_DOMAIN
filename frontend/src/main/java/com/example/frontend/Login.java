package com.example.frontend;

import com.example.frontend.models.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Login extends Application {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void start(Stage stage) {
        Text titleText = new Text("CINEVERSE");
        titleText.getStyleClass().add("title-text");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(230);
        usernameField.getStyleClass().add("text-field");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(230);
        passwordField.getStyleClass().add("text-field");

        TextField passwordVisibleField = new TextField();
        passwordVisibleField.setPromptText("Password");
        passwordVisibleField.setMaxWidth(230);
        passwordVisibleField.getStyleClass().add("text-field");
        passwordVisibleField.setManaged(false);
        passwordVisibleField.setVisible(false);

        // Synchronize text between password fields
        passwordField.textProperty().bindBidirectional(passwordVisibleField.textProperty());

        CheckBox showPasswordCheckBox = new CheckBox("Show Password");
        showPasswordCheckBox.getStyleClass().add("check-box");
        showPasswordCheckBox.setOnAction(event -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordVisibleField.setVisible(true);
                passwordVisibleField.setManaged(true);
                passwordField.setVisible(false);
                passwordField.setManaged(false);
            } else {
                passwordVisibleField.setVisible(false);
                passwordVisibleField.setManaged(false);
                passwordField.setVisible(true);
                passwordField.setManaged(true);
            }
        });

        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(200);
        loginButton.setPrefHeight(20);
        loginButton.getStyleClass().add("login-button");

        passwordField.setOnAction(event -> loginButton.fire());
        passwordVisibleField.setOnAction(event -> loginButton.fire());

        Label messageLabel = new Label();
        messageLabel.getStyleClass().add("message-label");

        Hyperlink registerLink = new Hyperlink("Don't have an account? Signup");
        registerLink.getStyleClass().add("register-link");
        registerLink.setOnAction(event -> openRegisterPage(stage));

        loginButton.setOnAction(event -> handleLogin(usernameField, passwordField, messageLabel));

        VBox vbox = new VBox(10, titleText, usernameField, passwordField, passwordVisibleField, showPasswordCheckBox,
                loginButton, messageLabel, registerLink);
        vbox.setAlignment(Pos.CENTER);
        vbox.getStyleClass().add("root");

        Scene scene = new Scene(vbox, 530, 500);
        scene.getStylesheets().add(getClass().getResource("/css/login.css").toExternalForm());

        stage.setTitle("Cineverse");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResource("/images/popcornicon.png").toExternalForm()));
        stage.show();
    }

    private void handleLogin(TextField usernameField, PasswordField passwordField, Label messageLabel) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        User user = MoviesClient.getUserByUsername(username);

        LoginRequest loginRequest = new LoginRequest(username, password);
        try {
            String requestBody = objectMapper.writeValueAsString(loginRequest);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/users/login")) // Change URL to your backend
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() == 200) {
                            Platform.runLater(() -> openMoviesPage((Stage) usernameField.getScene().getWindow()));
                        } else {
                            Platform.runLater(() -> {
                                messageLabel.setText("Invalid username or password.");
                                messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-font-weight: bold;");
                            });
                        }
                    })
                    .exceptionally(e -> {
                        Platform.runLater(() -> {
                            messageLabel.setText("Error connecting to server.");
                            messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px; -fx-font-weight: bold;");
                        });
                        e.printStackTrace();
                        return null;
                    });

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error preparing login request.");
        }

        UserSession.createSession(username, user.getUserId());
    }

    private void openMoviesPage(Stage currentStage) {
        Movies moviesPage = new Movies();
        Stage moviesStage = new Stage();
        try {
            moviesPage.start(moviesStage);
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openRegisterPage(Stage currentStage) {
        Register registerPage = new Register();
        Stage registerStage = new Stage();
        try {
            registerPage.start(registerStage);
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DTO for login request JSON serialization
    public static class LoginRequest {
        private final String username;
        private final String password;

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
        public String getUsername() { return username; }
        public String getPassword() { return password; }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
