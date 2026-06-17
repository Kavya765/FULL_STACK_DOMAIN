package com.example.cineverse.controllers;

import com.example.cineverse.dto.PostResponse;
import com.example.cineverse.dto.ReplyResponse;
import com.example.cineverse.models.Post;
import com.example.cineverse.models.Reply;
import com.example.cineverse.services.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/replies")
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @PostMapping
    public Reply createReply(@RequestBody ReplyResponse replyResponse) {
        System.out.println(replyResponse);
        return replyService.saveReply(replyResponse);
    }

    @GetMapping("/post/{postId}")
    public List<ReplyResponse> getRepliesByPost(@PathVariable int postId) {
        List<Reply> replies = replyService.getRepliesByPostId(postId);

        List<ReplyResponse> response = replies.stream()
                .filter(reply -> reply != null && reply.getReplyText() != null && !reply.getReplyText().isBlank())
                .map(ReplyResponse::new)
                .toList();

        return response;
    }

    @GetMapping("/user/{userId}")
    public List<Reply> getRepliesByUser(@PathVariable int userId) {
        return replyService.getRepliesByUserId(userId);
    }

    @GetMapping("/{id}")
    public Reply getReplyById(@PathVariable int id) {
        return replyService.getReplyById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteReply(@PathVariable int id) {
        replyService.deleteReply(id);
    }
}
