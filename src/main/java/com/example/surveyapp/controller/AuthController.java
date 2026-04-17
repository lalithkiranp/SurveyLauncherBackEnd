package com.example.surveyapp.controller;

import com.example.surveyapp.dto.LoginRequest;
import com.example.surveyapp.entity.User;
import com.example.surveyapp.repository.UserRepository;
import com.example.surveyapp.security.JwtUtil;
import com.example.surveyapp.service.TokenBlacklistService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TokenBlacklistService blacklistService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(user.getUsername(), user.getRole());
    }
    
    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            blacklistService.blacklistToken(token);
        }

        return "Logged out successfully";
    }
}