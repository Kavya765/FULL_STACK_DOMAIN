package com.example.frontend;

import com.example.frontend.models.Movie;
import com.example.frontend.models.Review;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProfileClient {
    private static final String BASE_URL = "http://localhost:8080/api";

    public static List<Movie> getWatchedMovies(int userId) {
        try {
            URL url = new URL(BASE_URL + "/watched/user/" + userId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) response.append(line);
            in.close();

            // Create Gson with LocalDate support
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                        @Override
                        public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext context) {
                            return LocalDate.parse(json.getAsString());
                        }
                    })
                    .create();

            // Parse the JSON
            Type movieListType = new TypeToken<List<Movie>>(){}.getType();
            return gson.fromJson(response.toString(), movieListType);

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public static List<Movie> getWatchlistMovies(int userId) {
        try {
            URL url = new URL(BASE_URL + "/watchlist/user/" + userId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) response.append(line);
            in.close();

            Type movieListType = new TypeToken<List<Movie>>() {}.getType();
            return getGsonWithLocalDateSupport().fromJson(response.toString(), movieListType);

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<Movie> getFavoriteMovies(int userId) {
        try {
            URL url = new URL(BASE_URL + "/favorites/user/" + userId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) response.append(line);
            in.close();

            Type movieListType = new TypeToken<List<Movie>>() {}.getType();
            return getGsonWithLocalDateSupport().fromJson(response.toString(), movieListType);

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<Review> getUserReviews(int userId) {
        try {

            URL url = new URL(BASE_URL + "/reviews/user/" + userId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) response.append(line);
            in.close();

            Type reviewListType = new TypeToken<List<Review>>() {}.getType();

            return getGsonWithLocalDateSupport().fromJson(response.toString(), reviewListType);

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    private static Gson getGsonWithLocalDateSupport() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>)
                        (json, type, context) -> LocalDate.parse(json.getAsString()))
                .create();
    }




}
