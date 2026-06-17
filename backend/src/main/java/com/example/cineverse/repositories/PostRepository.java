package com.example.cineverse.repositories;

import com.example.cineverse.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    Post findById(int postId);
    List<Post> findByMovieId(int movieId);          // all posts for a movie
    List<Post> findByUserId(int userId);            // all posts by a user
    boolean existsByUserIdAndMovieId(int userId, int movieId); // did user already post?
}
