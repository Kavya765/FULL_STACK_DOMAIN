package com.example.frontend.models;

import java.sql.Timestamp;

public class Reply {
    private int id;
    private int postId;
    private int userId;
    private String replyText;
    private String username;
    private int likes;
    private Timestamp created_at;

    public Reply(int postId, int userId, String replyText) {
        this.postId = postId;
        this.userId = userId;
        this.replyText = replyText;
    }

    public Reply(int id, int postId, int userId, String replyText, String username, int likes, Timestamp created_at) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.replyText = replyText;
        this.username = username;
        this.likes = likes;
        this.created_at = created_at;
    }

    public Reply(int id, int postId, int userId, String replyText, int likes, Timestamp created_at) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.replyText = replyText;
        this.likes = likes;
        this.created_at = created_at;
    }

    public Reply() {

    }

    // Getters and Setters
    public int getReplyId() {
        return id;
    }

    public void setReplyId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Reply{" +
                "replyId=" + id +
                ", postId=" + postId +
                ", userId=" + userId +
                ", replyText='" + replyText + '\'' +
                ", username='" + username + '\'' +
                ", likes=" + likes +
                ", created_at=" + created_at +
                '}';
    }
}
