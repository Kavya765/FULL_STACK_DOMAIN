package com.example.cineverse.controllers;

import com.example.cineverse.dto.ReplyLikeResponse;
import com.example.cineverse.models.ReplyLike;
import com.example.cineverse.services.ReplyLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/replylikes")
public class ReplyLikeController {

    @Autowired
    private ReplyLikeService replyLikeService;

    @GetMapping("/user/{userId}")
    public List<ReplyLike> getLikesByUser(@PathVariable int userId) {
        return replyLikeService.getLikesByUserId(userId);
    }

    @GetMapping("/userId/{userId}/replyId/{replyId}")
    public boolean hasUserLikedReply(@PathVariable int userId, @PathVariable int replyId) {
        return replyLikeService.hasUserLikedReply(userId, replyId);
    }

    @GetMapping("/count/{replyId}")
    public int countLikes(@PathVariable int replyId) {
        return replyLikeService.countLikesOnReply(replyId);
    }

    @PostMapping("/")
    public ReplyLike likeReply(@RequestBody ReplyLikeResponse replyLikeResponse) {
        return replyLikeService.saveReplyLike(replyLikeResponse);
    }

    @DeleteMapping("/userId/{userId}/replyId/{replyId}")
    public void deleteLike(@PathVariable int userId, @PathVariable int replyId) {
        replyLikeService.deleteLike(userId, replyId);
    }
}
