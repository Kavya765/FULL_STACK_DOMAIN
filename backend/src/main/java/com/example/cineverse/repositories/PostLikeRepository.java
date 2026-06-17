package com.example.cineverse.repositories;

import com.example.cineverse.models.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Integer> {

    boolean existsByPostIdAndUserId(int postId, int userId);   // has user liked this post?
    int countByPostId(int postId);                             // like count for a post
    List<PostLike> findByUserId(int userId);
    Optional<PostLike> findByUserIdAndPostId(int userId, int postId);

    Optional<PostLike> findByPostIdAndUserId(int postId, int userId);
}
