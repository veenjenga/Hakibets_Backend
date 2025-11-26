package com.hakibets.menu_dashboard.controller;

import com.hakibets.menu_dashboard.entity.User;
import com.hakibets.menu_dashboard.service.UserService;
import com.hakibets.menu_dashboard.dto.LoginRequest;
import com.hakibets.menu_dashboard.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserService userService, JwtUtil jwtUtil, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        System.out.println("AuthController initialized at " + new java.util.Date());
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String username, @RequestParam String password) {
        System.out.println("Registering user: " + username);
        userService.registerUser(username, password);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        System.out.println("Login attempt for username: " + request.getUsername() + " at " + new java.util.Date());
        try {
            User user = userService.findByUsername(request.getUsername());
            System.out.println("User found: " + (user != null ? user.getUsername() : "null"));
            if (user == null) {
                System.out.println("Login failed: User not found");
                return ResponseEntity.status(401).body("Invalid credentials");
            }
            System.out.println("Stored password hash: " + user.getPassword());
            System.out.println("Provided password: " + request.getPassword());
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                System.out.println("Password matched, generating token");
                String token = jwtUtil.generateToken(user.getUsername());
                System.out.println("Token generated: " + token);
                return ResponseEntity.ok(token);
            } else {
                System.out.println("Login failed: Password mismatch");
                return ResponseEntity.status(401).body("Invalid credentials");
            }
        } catch (Exception e) {
            System.out.println("Exception during login: " + e.getMessage());
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }
}