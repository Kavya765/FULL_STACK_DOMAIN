package com.example.cineverse.controllers;

import com.example.cineverse.dto.PostResponse;
import com.example.cineverse.models.Post;
import com.example.cineverse.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public Post createPost(@RequestBody PostResponse postResponse) {
        return postService.savePost(postResponse);
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable int id) {
        return postService.getPostById(id);
    }

    @GetMapping("/movie/{movieId}")
    public List<PostResponse> getPostsByMovie(@PathVariable int movieId) {
        List<Post> posts = postService.getPostsByMovieId(movieId);
        List<PostResponse> response = posts.stream()
                .map(PostResponse::new)
                .toList();
        return response;
    }

    @GetMapping("/user/{userId}")
    public List<Post> getPostsByUser(@PathVariable int userId) {
        return postService.getPostsByUserId(userId);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable int id) {
        postService.deletePost(id);
    }
}
