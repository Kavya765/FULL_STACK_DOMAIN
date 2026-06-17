package com.example.cineverse.dto;

import com.example.cineverse.models.Post;

import java.sql.Timestamp;

public class PostResponse {
    private int postId;
    private int userId;
    private int movieId;
    private String postText;
    private String username;
    private Timestamp createdAt;

    public PostResponse() {}

    public PostResponse(int userId, int movieId, String postText) {
        this.userId = userId;
        this.movieId = movieId;
        this.postText = postText;
    }

    public PostResponse(Post post) {
        this.postId = post.getPostId();
        this.userId = post.getUser().getUserId();
        this.movieId = post.getMovie().getMovieId();
        this.postText = post.getPostText();
        this.username = post.getUser().getUsername();
        this.createdAt = post.getCreatedAt();
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getPostId() {
        return postId;
    }

    public int getUserId() {
        return userId;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getPostText() {
        return postText;
    }

    public String getUsername() {
        return username;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "PostResponse{" +
                "postId=" + postId +
                ", userId=" + userId +
                ", movieId=" + movieId +
                ", postText='" + postText + '\'' +
                ", username='" + username + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

