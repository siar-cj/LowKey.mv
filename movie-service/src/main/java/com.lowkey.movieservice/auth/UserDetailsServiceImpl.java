package com.lowkey.movieservice.auth;

import com.lowkey.movieservice.model.User;
import com.lowkey.movieservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user from the database
        User user = userRepository.findByUsername(username);

        // Ensure the user exists and is active
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        if (!user.isActive()) {
            throw new UsernameNotFoundException("Account is not confirmed. Please confirm your email.");
        }

        // Return a Spring Security User object with roles and authorities
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().toArray(new String[0])) // Convert roles to String array
                .build();
    }
}