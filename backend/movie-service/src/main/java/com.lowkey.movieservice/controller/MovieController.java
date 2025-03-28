package com.lowkey.movieservice.controller;

import com.lowkey.movieservice.model.Movie;
import com.lowkey.movieservice.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    // Fetch all movies (accessible to everyone, including guests)
    @GetMapping
    public List<Movie> getAllMovies() {
        return movieService.fetchAllMovies();
    }

    // Fetch a specific movie by ID (accessible to everyone, including guests)
    @GetMapping("/{id}")
    public Optional<Movie> getMovieById(@PathVariable Long id) {
        return movieService.fetchMovieById(id);
    }

    // Add or update a movie (accessible only to logged-in users with USER or ADMIN roles)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    public Movie addOrUpdateMovie(@RequestBody Movie movie, @RequestHeader("userRole") String userRole) {
        return movieService.saveMovie(movie, userRole);
    }

    // Delete a movie (accessible only to users with ADMIN role)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id, @RequestHeader("userRole") String userRole) {
        movieService.deleteMovie(id, userRole);
    }

    // Fetch movies from an external API (accessible only to logged-in users with USER or ADMIN roles)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/external")
    public List<Movie> fetchMoviesFromExternalApi(@RequestHeader("userRole") String userRole) {
        return movieService.fetchMoviesFromExternalApi(userRole);
    }
}