package com.example.frontend;

public class UserSession {
    private static UserSession instance;
    private String username;
    private int userId;

    private UserSession(String username, int userId) {
        this.username = username;
        this.userId = userId;
    }

    public static void createSession(String username, int userId) {
        instance = new UserSession(username, userId);
    }

    public static UserSession getInstance() {
        return instance;
    }

    public static void clearSession() {
        instance = null;
    }

    public String getUsername() {
        return username;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isLoggedIn() {
        return instance != null;
    }

}

