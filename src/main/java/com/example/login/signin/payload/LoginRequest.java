package com.example.login.signin.payload;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
