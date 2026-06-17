package com.example.cineverse.services;

import com.example.cineverse.dto.PostLikeResponse;
import com.example.cineverse.models.Post;
import com.example.cineverse.models.PostLike;
import com.example.cineverse.models.User;
import com.example.cineverse.repositories.PostLikeRepository;
import com.example.cineverse.repositories.PostRepository;
import com.example.cineverse.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostLikeService {

    @Autowired
    private PostLikeRepository postLikeRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    public PostLike savePostLike(PostLikeResponse postLikeResponse) {
        PostLike postLike = new PostLike();

        postLike.setUser(userRepository.findById(postLikeResponse.getUserId()));
        postLike.setPost(postRepository.findById(postLikeResponse.getPostId()));

        return postLikeRepository.save(postLike);
    }

    public boolean hasUserLikedPost(int userId, int postId) {
        return postLikeRepository.existsByPostIdAndUserId(postId, userId);
    }

    public int countLikesOnPost(int postId) {
        return postLikeRepository.countByPostId(postId);
    }

    public List<PostLike> getLikesByUserId(int userId) {
        return postLikeRepository.findByUserId(userId);
    }

    public void deleteLike(int userId, int postId) {
        Optional<PostLike> like = postLikeRepository.findByUserIdAndPostId(userId, postId);
        like.ifPresent(postLikeRepository::delete);
    }

}
