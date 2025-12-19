package com.expensetracker.dto;

public class LoginResponse {

    private String token;
    private String username;
    private String name;
    private String email;
    private boolean admin;

    // Construtor
    public LoginResponse() {}

    public LoginResponse(String token, String username, String name, String email, boolean admin) {
        this.token = token;
        this.username = username;
        this.name = name;
        this.email = email;
        this.admin = admin;
    }

    // Getters e Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public boolean isAdmin() { return admin; }
    public void setAdmin(boolean admin) { this.admin = admin; }
}