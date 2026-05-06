package com.example.lab8.model;

public class User {
    private String email;
    private String password;
    private String role;
    private String userID;

    public User() {
    }

    public User(String email, String password, String role, String userID) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.userID = userID;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }
}
