package com.example.frontend.models;

import java.sql.Timestamp;

public class Post {
    private int postId;
    private int movieId;
    private int userId;
    private String username;
    private String postText;
    private int likes;
    private Timestamp created_at;

    public Post(){}

    public Post(int movieId, int userId,String postText) {
        this.movieId = movieId;
        this.userId = userId;
        this.postText = postText;
    }

    public Post(int movieId, int userId,String postText, String username) {
        this.movieId = movieId;
        this.userId = userId;
        this.postText = postText;
        this.username = username;
    }

    public Post(int postId,int movieId, int userId, String postText, int likes, Timestamp created_at, String username) {
        this.postId = postId;
        this.movieId = movieId;
        this.userId = userId;
        this.postText = postText;
        this.username = username;
        this.likes = likes;
        this.created_at = created_at;
    }

    // Getters and Setters
    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Timestamp getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Timestamp created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", movieId=" + movieId +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", postText='" + postText + '\'' +
                ", likes=" + likes +
                ", created_at=" + created_at +
                '}';
    }
}
