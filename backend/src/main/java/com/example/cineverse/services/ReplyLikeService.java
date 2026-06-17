package com.example.cineverse.services;

import com.example.cineverse.dto.ReplyLikeResponse;
import com.example.cineverse.models.Post;
import com.example.cineverse.models.PostLike;
import com.example.cineverse.models.Reply;
import com.example.cineverse.models.ReplyLike;
import com.example.cineverse.repositories.PostLikeRepository;
import com.example.cineverse.repositories.ReplyLikeRepository;
import com.example.cineverse.repositories.ReplyRepository;
import com.example.cineverse.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReplyLikeService {

    @Autowired
    private ReplyLikeRepository replyLikeRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private UserRepository userRepository;


    public ReplyLike saveReplyLike(ReplyLikeResponse replyLikeResponse) {
        ReplyLike replyLike = new ReplyLike();

        replyLike.setUser(userRepository.findById(replyLikeResponse.getUserId()));
        replyLike.setReply(replyRepository.findById(replyLikeResponse.getReplyId()));

        return replyLikeRepository.save(replyLike);
    }

    public boolean hasUserLikedReply(int userId, int replyId) {
        return replyLikeRepository.existsByReplyIdAndUserId(replyId, userId);
    }


    public int countLikesOnReply(int replyId) {
        return replyLikeRepository.countByReplyId(replyId);
    }

    public List<ReplyLike> getLikesByUserId(int userId) {
        return replyLikeRepository.findByUserId(userId);
    }

    public void deleteLike(int userId, int replyId) {
        Optional<ReplyLike> like = replyLikeRepository.findByUserIdAndReplyId(userId, replyId);
        like.ifPresent(replyLikeRepository::delete);
    }
}
