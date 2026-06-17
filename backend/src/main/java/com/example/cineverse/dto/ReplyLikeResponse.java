package com.example.cineverse.dto;

public class ReplyLikeResponse {
    private int id;
    private int userId;
    private int replyId;

    public ReplyLikeResponse(int userId, int replyId) {
        this.userId = userId;
        this.replyId = replyId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    @Override
    public String toString() {
        return "ReplyLikeResponse{" +
                "id=" + id +
                ", userId=" + userId +
                ", replyId=" + replyId +
                '}';
    }
}
