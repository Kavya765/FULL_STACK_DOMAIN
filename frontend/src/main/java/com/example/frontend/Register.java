package com.example.frontend;

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

public class Register extends Application {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void start(Stage stage) {
        Text titleText = new Text("REGISTER YOUR ACCOUNT");
        titleText.getStyleClass().add("title-text");

        // Create the input fields
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(230);
        usernameField.getStyleClass().add("text-field");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(230);
        emailField.getStyleClass().add("text-field");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(230);
        passwordField.getStyleClass().add("text-field");

        // Create the signup button
        Button signupButton = new Button("Sign Up");
        signupButton.setPrefWidth(200);
        signupButton.setPrefHeight(20);
        signupButton.getStyleClass().add("signup-button");

        passwordField.setOnAction(event -> signupButton.fire());

        // Create a label to show messages (error or success)
        Label messageLabel = new Label();
        messageLabel.getStyleClass().add("message-label");

        // Add event handler for the signup button
        signupButton.setOnAction(event -> handleSignup(usernameField, emailField, passwordField, messageLabel));

        // Create a hyperlink for login
        Hyperlink loginLink = new Hyperlink("Already have an account? Login");
        loginLink.getStyleClass().add("login-link");
        loginLink.setOnAction(event -> openLoginPage(stage));

        // Create a vertical box layout to arrange the components
        VBox vbox = new VBox(10, titleText, usernameField, emailField, passwordField, signupButton, messageLabel, loginLink);
        vbox.setAlignment(Pos.CENTER);
        vbox.getStyleClass().add("root");

        Scene scene = new Scene(vbox, 530, 600);
        scene.getStylesheets().add(getClass().getResource("/css/register.css").toExternalForm());

        stage.setTitle("Cineverse");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResource("/images/popcornicon.png").toExternalForm()));
        stage.show();
    }

    private void handleSignup(TextField usernameField, TextField emailField, PasswordField passwordField, Label messageLabel) {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        RegisterRequest registerRequest = new RegisterRequest(username, email, password);

        try {
            String requestBody = objectMapper.writeValueAsString(registerRequest);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/users/register")) // Adjust backend URL if needed
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() == 201) { // HTTP 201 Created means success
                            Platform.runLater(() -> {
                                showAlert("Success", "Registration successful! You can now login.");
                                openLoginPage((Stage) usernameField.getScene().getWindow());
                            });
                        } else {
                            Platform.runLater(() -> {
                                messageLabel.setText("Registration failed: " + response.body());
                                messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                            });
                        }
                    })
                    .exceptionally(e -> {
                        Platform.runLater(() -> {
                            messageLabel.setText("Error connecting to server.");
                            messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                        });
                        e.printStackTrace();
                        return null;
                    });

        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error preparing registration request.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Open the Login page
    private void openLoginPage(Stage currentStage) {
        Login loginPage = new Login();
        Stage loginStage = new Stage();
        try {
            loginPage.start(loginStage);
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class RegisterRequest {
        public String username;
        public String email;
        public String password;

        public RegisterRequest(String username, String email, String password) {
            this.username = username;
            this.email = email;
            this.password = password;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

