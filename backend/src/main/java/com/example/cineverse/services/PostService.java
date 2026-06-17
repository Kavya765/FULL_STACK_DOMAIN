package com.example.cineverse.services;

import com.example.cineverse.dto.PostResponse;
import com.example.cineverse.models.Post;
import com.example.cineverse.repositories.MovieRepository;
import com.example.cineverse.repositories.PostRepository;
import com.example.cineverse.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private UserRepository userRepository;

    public Post savePost(PostResponse postResponse) {
        Post post = new Post();

        post.setPostText(postResponse.getPostText());
        post.setMovie(movieRepository.findById(postResponse.getMovieId()));
        post.setUser(userRepository.findById(postResponse.getUserId()));

        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(int id) {
        return postRepository.findById(id);
    }

    public List<Post> getPostsByMovieId(int movieId) {
        return postRepository.findByMovieId(movieId);
    }

    public List<Post> getPostsByUserId(int userId) {
        return postRepository.findByUserId(userId);
    }

    public boolean hasUserPostedOnMovie(int userId, int movieId) {
        return postRepository.existsByUserIdAndMovieId(userId, movieId);
    }

    public void deletePost(int id) {
        postRepository.deleteById(id);
    }
}
