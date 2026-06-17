package com.example.cineverse.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;

@Entity
@Table(name = "reply_likes")
public class ReplyLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "reply_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Reply reply;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    public ReplyLike() {}

    public ReplyLike(Reply reply, User user, Timestamp createdAt) {
        this.reply  = reply;
        this.user   = user;
        this.createdAt = createdAt;
    }

    public int getId()              { return id; }
    public void setId(int id)       { this.id = id; }

    public Reply getReply()             { return reply; }
    public void setReply(Reply reply)   { this.reply = reply; }

    public User getUser()               { return user; }
    public void setUser(User user)      { this.user = user; }

    public Timestamp getCreatedAt()     { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}

