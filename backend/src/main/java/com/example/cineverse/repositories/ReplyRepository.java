package com.example.cineverse.repositories;

import com.example.cineverse.models.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Integer> {

    Reply findById(int replyId);
    List<Reply> findByPostId(int postId);           // replies under a post
    List<Reply> findByUserId(int userId);           // replies written by a user
    boolean existsByUserIdAndPostId(int userId, int postId);   // did user already reply?
}
