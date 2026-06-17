package com.example.cineverse.services;

import com.example.cineverse.dto.ReplyResponse;
import com.example.cineverse.models.Reply;
import com.example.cineverse.repositories.PostRepository;
import com.example.cineverse.repositories.ReplyRepository;
import com.example.cineverse.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    public Reply saveReply(ReplyResponse replyResponse) {
        Reply reply = new Reply();

        reply.setReplyText(replyResponse.getReplyText());

        reply.setUser(userRepository.findById(replyResponse.getUserId()));
        reply.setPost(postRepository.findById(replyResponse.getPostId()));

        return replyRepository.save(reply);
    }

    public List<Reply> getRepliesByPostId(int postId) {
        return replyRepository.findByPostId(postId);
    }

    public List<Reply> getRepliesByUserId(int userId) {
        return replyRepository.findByUserId(userId);
    }

    public Reply getReplyById(int id) {
        return replyRepository.findById(id);
    }

    public boolean hasUserRepliedToPost(int userId, int postId) {
        return replyRepository.existsByUserIdAndPostId(userId, postId);
    }

    public void deleteReply(int id) {
        replyRepository.deleteById(id);
    }
}
