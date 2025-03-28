package com.lowkey.movieservice.controller;

import com.lowkey.movieservice.model.User;
import com.lowkey.movieservice.model.ConfirmationToken;
import com.lowkey.movieservice.repository.UserRepository;
import com.lowkey.movieservice.repository.ConfirmationTokenRepository;
import com.lowkey.movieservice.service.EmailService;
import com.lowkey.movieservice.auth.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    // User registration with Gmail confirmation
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        // Validate if email is already registered
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Email is already registered.");
        }

        // Hash the password and set the user as inactive
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setActive(false);
        userRepository.save(user);

        // Generate confirmation token
        ConfirmationToken token = new ConfirmationToken(
                UUID.randomUUID().toString(),
                new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24), // 24 hours validity
                user
        );
        tokenRepository.save(token);

        // Send confirmation email
        emailService.sendConfirmationEmail(user.getUsername(), token.getToken());

        return "Registration successful. Please check your email to confirm your account.";
    }

    // Account confirmation via email
    @GetMapping("/confirm")
    public String confirmAccount(@RequestParam String token) {
        ConfirmationToken confirmationToken = tokenRepository.findByToken(token);
        if (confirmationToken != null && confirmationToken.getExpiryDate().after(new Date())) {
            User user = confirmationToken.getUser();
            user.setActive(true); // Activate the account
            userRepository.save(user);
            return "Account confirmed successfully!";
        }
        return "Invalid or expired confirmation link.";
    }

    // User login
    @PostMapping("/login")
    public String loginUser(@RequestBody User loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());

        if (user == null || !new BCryptPasswordEncoder().matches(loginRequest.getPassword(), user.getPassword())) {
            return "Invalid username or password.";
        }

        if (!user.isActive()) {
            return "Account is not confirmed. Please confirm your email.";
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getUsername(), user.isActive(), user.getRoles().iterator().next());
        return "Login successful. Your token: " + token;
    }
}