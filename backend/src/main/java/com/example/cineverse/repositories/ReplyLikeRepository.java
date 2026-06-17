package com.example.cineverse.repositories;

import com.example.cineverse.models.PostLike;
import com.example.cineverse.models.ReplyLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Integer> {

    boolean existsByReplyIdAndUserId(int replyId, int userId);
    int countByReplyId(int replyId);                           // like count for a reply
    List<ReplyLike> findByUserId(int userId);
    Optional<ReplyLike> findByUserIdAndReplyId(int userId, int replyId);// replies liked by user
}
