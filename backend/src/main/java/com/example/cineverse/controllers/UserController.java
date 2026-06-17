package com.example.cineverse.controllers;

import com.example.cineverse.dto.LoginRequest;
import com.example.cineverse.dto.LoginResponse;
import com.example.cineverse.dto.RegisterRequest;
import com.example.cineverse.dto.RegisterResponse;
import com.example.cineverse.models.User;
import com.example.cineverse.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        return user;
    }

    @GetMapping("/username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        return user.orElse(null);
    }

    @GetMapping("/")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/")
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            userService.registerUser(registerRequest);
            RegisterResponse response = new RegisterResponse(true, "User registered successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            RegisterResponse response = new RegisterResponse(false, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            RegisterResponse response = new RegisterResponse(false, "Registration failed.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PutMapping("/{id}")
    public boolean updateUser(@RequestBody User user) {
        boolean updated = userService.updateUser(user);
        return updated;
    }

    @DeleteMapping("/username/{username}")
    public boolean deleteUser(@PathVariable String username) {
        System.out.println("Deleting user: " + username);
        return userService.deleteByUsername(username);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        boolean authenticated = userService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());

        if (authenticated) {
            // You could generate a JWT or some session token here if you want
            LoginResponse response = new LoginResponse(true, "Login successful");
            return ResponseEntity.ok(response);
        } else {
            LoginResponse response = new LoginResponse(false, "Invalid username or password");
            return ResponseEntity.status(401).body(response);
        }
    }

    @GetMapping("/exists/{username}")
    public boolean checkUsernameExists(@PathVariable String username) {
        return userService.existsByUsername(username);
    }
}
