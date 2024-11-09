package com.example.assisment.data.models;

public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest(String email, String password) {
        this.username = email;
        this.password = password;
    }
}
