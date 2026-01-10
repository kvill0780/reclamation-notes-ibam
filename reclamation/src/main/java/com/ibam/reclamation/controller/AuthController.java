package com.ibam.reclamation.controller;

import com.ibam.reclamation.dto.LoginRequest;
import com.ibam.reclamation.dto.LoginResponse;
import com.ibam.reclamation.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.authenticate(
                request.getEmail(),
                request.getPassword()
        );
    }
    
    @PostMapping("/logout")
    public String logout() {
        return "Logout endpoint";
    }
}