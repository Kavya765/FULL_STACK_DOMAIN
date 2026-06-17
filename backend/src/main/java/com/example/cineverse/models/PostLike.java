package com.example.cineverse.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Timestamp;
import java.util.Optional;

@Entity
@Table(name = "post_likes")
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)   // FK → posts.post_id
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)   // FK → users.user_id
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    public PostLike() {}

    public PostLike(Post post, User user, Timestamp createdAt) {
        this.post = post;
        this.user  = user;
        this.createdAt = createdAt;
    }

    /* getters & setters */
    public int getId()            { return id; }
    public void setId(int id)     { this.id = id; }

    public Post getPost()             { return post; }
    public void setPost(Post post)    { this.post = post; }

    public User getUser()             { return user; }
    public void setUser(User user)    { this.user = user; }

    public Timestamp getCreatedAt()   { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "PostLike{" +
                "id=" + id +
                ", post=" + post +
                ", user=" + user +
                ", createdAt=" + createdAt +
                '}';
    }
}