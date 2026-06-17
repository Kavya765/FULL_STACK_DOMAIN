package com.example.frontend;

import com.example.frontend.models.Movie;
import com.example.frontend.models.PostLike;
import com.example.frontend.models.Review;
import com.example.frontend.models.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MovieDetailsClient {

    private static final String BASE_URL = "http://localhost:8080/api";

    public static boolean isMovieFavorited(int userId, int movieId) {
        try {
            URL url = new URL(BASE_URL + "/favorites/check?userId=" + userId + "&movieId=" + movieId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine();
            in.close();

            return Boolean.parseBoolean(response);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isMovieWatched(int userId, int movieId) {
        try {
            URL url = new URL(BASE_URL + "/watched/user/" + userId + "/movie/" + movieId + "/exists");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    String result = reader.readLine();
                    return Boolean.parseBoolean(result);
                }
            } else {
                System.err.println("Watched check failed with response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean isMovieInWatchlist(int userId, int movieId) {
        try {
            URL url = new URL(BASE_URL + "/watchlist/user/" + userId + "/movie/" + movieId + "/exists");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    String result = reader.readLine();
                    return Boolean.parseBoolean(result);
                }
            } else {
                System.err.println("Watchlist check failed with response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void addToWatched(int userId, int movieId) {
        try {
            URL url = new URL(BASE_URL + "/watched/user/" + userId + "/movie/" + movieId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            // No body needed
            conn.connect();

            // Read the response (or just trigger the request)
            conn.getInputStream().close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addToWatchlist(int userId, int movieId) {
        try {
            URL url = new URL(BASE_URL + "/watchlist/user/" + userId + "/movie/" + movieId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            // No body needed
            conn.connect();

            // Read the response (or just trigger the request)
            conn.getInputStream().close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeWatched(int userId, int movieId) {
        try {
            URL url = new URL(BASE_URL + "/watched/user/" + userId + "/movie/" + movieId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            conn.getInputStream().close(); // Trigger the request

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeWatchlist(int userId, int movieId) {
        try {
            URL url = new URL(BASE_URL + "/watchlist/user/" + userId + "/movie/" + movieId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            conn.getInputStream().close(); // Trigger the request

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean updateFavoriteStatus(int userId, int movieId, boolean add) {
        try {
            // Build URL with query parameters
            String url = String.format("%s/favorites/update?userId=%d&movieId=%d&add=%b",
                    BASE_URL, userId, movieId, add);
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            // Get response
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()))) {
                    String response = in.readLine();

                    return true;
                }
            } else {
                try (BufferedReader err = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream()))) {
                    String error = err.readLine();
                    System.err.println("Favorite update failed: " + error);
                }
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean saveReview(int movieId, String reviewText, double rating) {
        try {
            URL url = new URL(BASE_URL + "/reviews");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Create review object
            Review review = new Review();
            review.setReviewText(reviewText);
            review.setRating(rating);

            // Create minimal movie object

            Movie movie = MoviesClient.getMovieById(movieId);
            review.setMovie(movie);

            //movie.setReleaseDate(movie.getReleaseDate());
            review.setMovie(movie);

            // Create minimal user object
            User user = new User();
            user.setUserId(UserSession.getInstance().getUserId());
            review.setUser(user);

            // Set current timestamp
            review.setCreatedAt(new Timestamp(System.currentTimeMillis()));


            // Use your configured Gson instance
            String jsonInput = getGsonWithLocalDateSupport().toJson(review);


            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            int responseCode = conn.getResponseCode();

            return responseCode >= 200 && responseCode < 300;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean hasUserReviewedMovie(int userId, int movieId) {
        try {
            // Build URL with query parameters
            String urlString = String.format("%s/reviews/exists?userId=%d&movieId=%d",
                    BASE_URL, userId, movieId);
            URL url = new URL(urlString);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Get response
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()))) {
                    // Read boolean response
                    return Boolean.parseBoolean(in.readLine());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Default to false if any error occurs
    }
    public static List<Review> getReviewsByMovie(int movieId) {

        try {
            URL url = new URL(BASE_URL + "/reviews/movie/" + movieId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read response
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }


                // Parse JSON to List<Review>
                Type reviewListType = new TypeToken<List<Review>>(){}.getType();
                return getGsonWithLocalDateSupport().fromJson(response.toString(), reviewListType);

            }

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList(); // Return empty list on error
        }
    }
    private static Gson getGsonWithLocalDateSupport() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new TypeAdapter<LocalDate>() {
                    @Override
                    public void write(JsonWriter out, LocalDate value) throws IOException {
                        out.value(value.toString());
                    }

                    @Override
                    public LocalDate read(JsonReader in) throws IOException {
                        return LocalDate.parse(in.nextString());
                    }
                })
                .create();
    }
    }

