package com.example.cineverse.services;

import com.example.cineverse.dto.RegisterRequest;
import com.example.cineverse.models.User;
import com.example.cineverse.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public boolean deleteByUsername(String username) {
        return userRepository.deleteByUsername(username) > 0;
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public User saveUser(User user) {
        // JpaRepository already has save() method so no need to define saveUser in repo interface
        return userRepository.save(user);
    }

    public boolean updateUser(User updatedUser) {

        if (updatedUser == null) {
            return false;
        }

        Optional<User> optUser = Optional.ofNullable(userRepository.findById(updatedUser.getUserId()));
        if (optUser.isEmpty()) {
            return false;
        }

        try {
            User existingUser = optUser.get();

            if (updatedUser.getUsername() != null && !updatedUser.getUsername().isEmpty()) {
                existingUser.setUsername(updatedUser.getUsername());
            }

            if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
                existingUser.setEmail(updatedUser.getEmail());
            }

            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(updatedUser.getPassword());
            }

            // Role can legitimately be null (keep existing) or a new value
            if (updatedUser.getRole() != null) {
                existingUser.setRole(updatedUser.getRole());
            }

            existingUser.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

            userRepository.save(existingUser);
            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public void registerUser(RegisterRequest request) {
        System.out.println("Received registration request: " + request.getUsername() + ", " + request.getEmail());
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already taken.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered.");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // Ideally hash this
        userRepository.save(user);

    }

    public boolean authenticate(String username, String rawPassword) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            return false;  // user not found
        }

        User user = userOpt.get();

        // Verify password with BCrypt
        return rawPassword.equals(user.getPassword());
    }

    public User getUserById(int id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
