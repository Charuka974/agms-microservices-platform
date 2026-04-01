package com.ugch.agms.api_gateway.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${jwt.secret}")
    private String secret;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        // Simple hardcoded check for the assignment demo
        if ("farmer".equals(credentials.get("username")) && "1234".equals(credentials.get("password"))) {

            String token = Jwts.builder()
                    .setSubject("FarmerJoe")
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 hour
                    .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .compact();

            // Wrap the token in a Map to return as JSON
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Login Successful");

            return ResponseEntity.ok(response);
        }

        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid Credentials");
        return ResponseEntity.status(401).body(error);
    }
}