package com.example.frontend;

import com.example.frontend.models.Post;
import com.example.frontend.models.PostLike;
import com.example.frontend.models.Reply;
import com.example.frontend.models.ReplyLike;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CommunityClient {

    private static final String BASE_URL = "http://localhost:8080/api";

    public static List<Post> getPostsByMovieId(int movieId) {
        try {
            URL url = new URL(BASE_URL + "/posts/movie/" + movieId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in  = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder  sb  = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) sb.append(line);
            in.close();

            // ---- Parse JSON manually (like getAllMovies) ----
            JsonArray jsonArray = JsonParser.parseString(sb.toString()).getAsJsonArray();
            List<Post> posts = new ArrayList<>();

            for (JsonElement el : jsonArray) {
                JsonObject obj = el.getAsJsonObject();
                Post post = new Post();

                post.setPostId(obj.get("postId").getAsInt());
                post.setMovieId(obj.get("movieId").getAsInt());
                post.setUserId(obj.get("userId").getAsInt());
                post.setUsername(obj.get("username").getAsString());
                post.setPostText(obj.get("postText").getAsString());

                // createdAt comes back as ISO string – convert to java.sql.Timestamp
                if (obj.has("createdAt") && !obj.get("createdAt").isJsonNull()) {
                    String iso = obj.get("createdAt").getAsString();          // e.g. 2025-05-24T14:06:12.000+00:00
                    String ts  = iso.replace("T", " ").substring(0, 19);      // -> 2025-05-24 14:06:12
                    post.setCreatedAt(java.sql.Timestamp.valueOf(ts));
                }

                posts.add(post);
            }
            return posts;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();   // empty list on error
        }
    }


    public static List<Reply> getRepliesByPostId(int postId) {
        try {
            URL url = new URL(BASE_URL + "/replies/post/" + postId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in  = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder  sb  = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) sb.append(line);
            in.close();

            // ---- Parse JSON manually (like getAllMovies) ----
            JsonArray jsonArray = JsonParser.parseString(sb.toString()).getAsJsonArray();
            List<Reply> replies = new ArrayList<>();

            for (JsonElement el : jsonArray) {
                JsonObject obj = el.getAsJsonObject();
                Reply reply = new Reply();

                if (obj.has("replyId") && !obj.get("replyId").isJsonNull()) {
                    reply.setReplyId(obj.get("replyId").getAsInt());
                }
                if (obj.has("postId") && !obj.get("postId").isJsonNull()) {
                    reply.setPostId(obj.get("postId").getAsInt());
                }
                if (obj.has("userId") && !obj.get("userId").isJsonNull()) {
                    reply.setUserId(obj.get("userId").getAsInt());
                }
                if (obj.has("username") && !obj.get("username").isJsonNull() && !obj.get("username").getAsString().isEmpty()) {
                    reply.setUsername(obj.get("username").getAsString());
                }
                if (obj.has("replyText") && !obj.get("replyText").isJsonNull() && !obj.get("replyText").getAsString().isEmpty()) {
                    reply.setReplyText(obj.get("replyText").getAsString());
                }
                if (obj.has("createdAt") && !obj.get("createdAt").isJsonNull() && !obj.get("createdAt").getAsString().isEmpty()) {
                    String iso = obj.get("createdAt").getAsString();          // e.g. 2025-05-24T14:06:12.000+00:00
                    String ts  = iso.replace("T", " ").substring(0, 19);      // -> 2025-05-24 14:06:12
                    reply.setCreated_at(java.sql.Timestamp.valueOf(ts));
                }



                replies.add(reply);
            }
            return replies;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();   // empty list on error
        }
    }

    public static boolean hasUserLikedPost(int userId, int postId) {
        try {
            URL url = new URL(BASE_URL + "/postlikes/userId/" + userId + "/postId/" + postId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine(); // Expecting a plain "true" or "false"
            in.close();

            return Boolean.parseBoolean(response);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean hasUserLikedReply(int userId, int replyId) {
        try {
            URL url = new URL(BASE_URL + "/replylikes/userId/" + userId + "/replyId/" + replyId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine(); // Expecting a plain "true" or "false"
            in.close();

            return Boolean.parseBoolean(response);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getPostLikesCount(int postId) {
        try {
            URL url = new URL(BASE_URL + "/postlikes/count/" + postId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine(); // Expected to be a plain integer
            in.close();

            return Integer.parseInt(response);

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getReplyLikesCount(int replyId) {
        try {
            URL url = new URL(BASE_URL + "/replylikes/count/" + replyId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.readLine(); // Expected to be a plain integer
            in.close();

            return Integer.parseInt(response);

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static void updatePostLike(int userId, int postId) {
        try {
            URL url = new URL(BASE_URL + "/postlikes/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true); // Enable body sending

            // Create JSON body
            String json = new Gson().toJson(new PostLike(postId, userId));

            // Send JSON body
            conn.getOutputStream().write(json.getBytes());
            conn.getOutputStream().flush();
            conn.getOutputStream().close();

            conn.getInputStream().close(); // Trigger the request

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void updateReplyLike(int userId, int replyId) {
        try {
            URL url = new URL(BASE_URL + "/replylikes/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true); // Enable body sending

            // Create JSON body
            ReplyLike replyLike = new ReplyLike(replyId, userId);

            String json = new Gson().toJson(replyLike);

            // Send JSON body
            conn.getOutputStream().write(json.getBytes());
            conn.getOutputStream().flush();
            conn.getOutputStream().close();

            conn.getInputStream().close(); // Trigger the request

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addPost(int userId, int movieId, String postText) {
        try {
            URL url = new URL(BASE_URL + "/posts");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true); // Enable writing body

            // Create JSON body
            String json = new Gson().toJson(new Post(movieId, userId, postText));

            // Write JSON to body
            conn.getOutputStream().write(json.getBytes());
            conn.getOutputStream().flush();
            conn.getOutputStream().close();

            conn.getInputStream().close(); // Trigger the request

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void addReply(int postId, int userId, String replyText) {
        try {
            URL url = new URL(BASE_URL + "/replies");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true); // Enable writing body

            // Create JSON body
            Reply reply = new Reply(postId,userId, replyText);
            String json = new Gson().toJson(reply);

            // Write JSON to body
            conn.getOutputStream().write(json.getBytes());
            conn.getOutputStream().flush();
            conn.getOutputStream().close();

            conn.getInputStream().close(); // Trigger the request

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeLikeFromPost(int userId, int postId) {
        try {
            URL url = new URL(BASE_URL + "/postlikes/userId/" + userId + "/postId/" + postId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            conn.getInputStream().close(); // Trigger the request

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeLikeFromReply(int userId, int replyId) {
        try {
            URL url = new URL(BASE_URL + "/replylikes/userId/" + userId + "/replyId/" + replyId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");

            conn.getInputStream().close(); // Trigger the request

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

