package com.example.frontend;

import com.example.frontend.models.Movie;
import com.example.frontend.models.User;
import com.example.frontend.models.Genre;
import com.google.gson.*;
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
import java.net.URLEncoder;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MoviesClient {
    private static final String BASE_URL = "http://localhost:8080/api";

    public static List<Movie> getAllMovies() {
        try {
            URL url = new URL(BASE_URL + "/movies");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) response.append(line);
            in.close();

            JsonArray jsonArray = JsonParser.parseString(response.toString()).getAsJsonArray();
            List<Movie> movies = new ArrayList<>();

            for (JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject();

                Movie movie = new Movie();

                movie.setId(obj.get("movieId").getAsInt());
                movie.setTitle(obj.get("title").getAsString());
                movie.setActors(obj.get("actors").getAsString());
                movie.setPosterUrl(obj.get("posterUrl").getAsString());
                movie.setDirector(obj.get("director").getAsString());
                movie.setrated(obj.get("rated").getAsString());
                movie.setReleaseDate(LocalDate.parse(obj.get("release_date").getAsString()));

                movies.add(movie);
            }
            return movies;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static User getUserById(int id) {
        try {
            URL url = new URL(BASE_URL + "/users/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) response.append(line);
            in.close();

            return new Gson().fromJson(response.toString(), User.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Movie getMovieById(int id) {
        try {
            URL url = new URL(BASE_URL + "/movies/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) response.append(line);
            in.close();

            // Use the properly configured Gson instance instead of new Gson()
            return getGsonWithLocalDateSupport().fromJson(response.toString(), Movie.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static User getUserByUsername(String username) {
        try {
            URL url = new URL(BASE_URL + "/users/username/" + URLEncoder.encode(username, "UTF-8"));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                System.err.println("Failed to get user: HTTP error code " + responseCode);
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            User user = new Gson().fromJson(br, User.class);
            br.close();

            return user;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void insertMovie(Movie movie) {
        try {
            URL url = new URL(BASE_URL + "/movies");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Register correct LocalDate serializer
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                        @Override
                        public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                            return new JsonPrimitive(src.toString());  // "yyyy-MM-dd"
                        }
                    })
                    .create();

            String jsonInput = gson.toJson(movie); // FIXED: use the custom gson here
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes());
                os.flush();
            }

            int code = conn.getResponseCode();
            if (code >= 200 && code < 300) {
                System.out.println("Movie inserted OK (" + code + ")");
            } else {
                System.err.println("Server returned " + code);
                try (BufferedReader err = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    err.lines().forEach(System.err::println);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean updateMovie(Movie movie) {
        try {
            URL url = new URL(BASE_URL + "/movies/" + movie.getId());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);


            // Use the same Gson instance with LocalDate serializer as insertMovie
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                        @Override
                        public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                            return new JsonPrimitive(src.toString());  // ISO format: yyyy-MM-dd
                        }
                    })
                    .create();

            String jsonInput = gson.toJson(movie);  // serialize with the custom Gson
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonInput.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateUser(User user) {
        try {
            URL url = new URL(BASE_URL + "/users/"+ user.getUserId());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String jsonInput = new Gson().toJson(user);
            OutputStream os = conn.getOutputStream();
            os.write(jsonInput.getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<Genre> getGenres() {
        try {
            URL url = new URL(BASE_URL + "/genres");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) response.append(line);
            in.close();


            Type type = new TypeToken<List<Genre>>() {}.getType();
            List<Genre> genres = new Gson().fromJson(response.toString(), type);
            return genres;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean createMovieGenre(int movieId, String genreName) {
        try {
            URL url = new URL(BASE_URL + "/moviegenres/movie/" + movieId + "/genre/" + genreName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true); // Usually good for POST

            int responseCode = conn.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    public static boolean deleteMovieByTitle(String title) {
        try {
            URL url = new URL(BASE_URL + "/movies/title/" + title);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            int responseCode = conn.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean deleteUserByUsername(String username) {
        try {
            URL url = new URL(BASE_URL + "/users/username/" + username);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            int responseCode = conn.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    public static void deleteUser(int id) {
        try {
            URL url = new URL(BASE_URL + "/users/delete/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> getMoviesGenre(int movieId) {
        try {
            URL url = new URL(BASE_URL + "/moviegenres/movie/" + movieId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) response.append(line);
            in.close();

            // Assuming Genre class exists with a `name` field
            Type type = new TypeToken<List<Genre>>() {}.getType();
            List<Genre> genres = new Gson().fromJson(response.toString(), type);

            List<String> genreNames = new ArrayList<>();
            for (Genre genre : genres) {
                genreNames.add(genre.getName());
            }
            return genreNames;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean deleteMovieById(int id) {
        try {
            URL url = new URL(BASE_URL + "/movies/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            int responseCode = conn.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_NO_CONTENT;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    private static Gson getGsonWithLocalDateSupport() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>)
                        (json, type, context) -> LocalDate.parse(json.getAsString()))
                .create();
    }
}
