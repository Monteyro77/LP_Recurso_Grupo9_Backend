package com.expensetracker.controller;

import com.expensetracker.dto.LoginRequest;
import com.expensetracker.dto.LoginResponse;
import com.expensetracker.model.User;
import com.expensetracker.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public void register(@RequestBody User user) {
        authService.register(user);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
