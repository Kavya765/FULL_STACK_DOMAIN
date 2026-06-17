package com.example.cineverse.controllers;

import com.example.cineverse.dto.PostLikeResponse;
import com.example.cineverse.models.PostLike;
import com.example.cineverse.services.PostLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/postlikes")
public class PostLikeController {

    @Autowired
    private PostLikeService postLikeService;

    @GetMapping("/user/{userId}")
    public List<PostLike> getLikesByUser(@PathVariable int userId) {
        return postLikeService.getLikesByUserId(userId);
    }

    @GetMapping("/userId/{userId}/postId/{postId}")
    public boolean hasUserLikedPost(@PathVariable int userId, @PathVariable int postId) {
        return postLikeService.hasUserLikedPost(userId, postId);
    }

    @GetMapping("/count/{postId}")
    public int countLikes(@PathVariable int postId) {
        return postLikeService.countLikesOnPost(postId);
    }

    @PostMapping("/")
    public PostLike likePost(@RequestBody PostLikeResponse postLike) {
        return postLikeService.savePostLike(postLike);
    }

    @DeleteMapping("/userId/{userId}/postId/{postId}")
    public void deleteLike(@PathVariable int userId, @PathVariable int postId) {
        postLikeService.deleteLike(userId, postId);
    }
}
