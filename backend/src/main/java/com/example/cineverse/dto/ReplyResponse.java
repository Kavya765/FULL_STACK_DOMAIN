package com.example.cineverse.dto;

import com.example.cineverse.models.Reply;

import java.sql.Timestamp;

public class ReplyResponse {
    private int replyId;
    private int userId;
    private int postId;
    private String replyText;
    private String username;
    private Timestamp createdAt;

    public ReplyResponse() {}

    public ReplyResponse(Reply reply) {
        this.replyId = reply.getReplyId();
        this.userId = reply.getUser().getUserId();
        this.postId = reply.getPost().getPostId();
        this.replyText = reply.getReplyText();
        this.username = reply.getUser().getUsername();
        this.createdAt = reply.getCreatedAt();
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
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

    public int getReplyId() {
        return replyId;
    }

    public String getReplyText() {
        return replyText;
    }

    public String getUsername() {
        return username;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "ReplyResponse{" +
                "replyId=" + replyId +
                ", postId=" + postId +
                ", userId=" + userId +
                ", replyText='" + replyText + '\'' +
                ", username='" + username + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

