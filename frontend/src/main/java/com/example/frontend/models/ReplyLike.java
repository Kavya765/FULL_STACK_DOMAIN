package com.example.frontend.models;

public class ReplyLike {
    private int replyId;
    private int userId;

    public ReplyLike(int replyId, int userId) {
        this.replyId = replyId;
        this.userId = userId;
    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ReplyLike{" +
                "replyId=" + replyId +
                ", userId=" + userId +
                '}';
    }
}

