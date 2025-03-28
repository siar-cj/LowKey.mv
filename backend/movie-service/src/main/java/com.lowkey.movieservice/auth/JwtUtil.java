package com.lowkey.movieservice.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "your_secret_key"; // Replace with a secure and private key

    // Generate a JWT token with claims
    public String generateToken(String username, boolean isActive, String role) {
        return Jwts.builder()
                .setSubject(username) // Store the username in the token
                .claim("isActive", isActive) // Include account active status
                .claim("role", role) // Include user role (e.g., USER, ADMIN)
                .setIssuedAt(new Date()) // Issue time
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours expiration
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // Sign the token with the secret key
                .compact();
    }

    // Validate the token for the given username and ensure it's not expired
    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        boolean isActive = (boolean) extractClaims(token).get("isActive"); // Check active status
        return extractedUsername.equals(username) && isActive && !isTokenExpired(token);
    }

    // Extract the username from the token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Extract the role from the token
    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    // Extract all claims from the token
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if the token is expired
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}