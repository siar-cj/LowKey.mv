package com.lowkey.movieservice.service;

import com.lowkey.movieservice.model.Movie;
import com.lowkey.movieservice.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String EXTERNAL_API_URL = "https://api.themoviedb.org/3/movie/popular?api_key=YOUR_API_KEY";

    // Fetch all movies (accessible to all users, including guests)
    public List<Movie> fetchAllMovies() {
        return movieRepository.findAll();
    }

    // Fetch a movie by its ID (accessible to all users)
    public Optional<Movie> fetchMovieById(Long id) {
        return movieRepository.findById(id);
    }

    // Add or update a movie (restricted to logged-in users only)
    public Movie saveMovie(Movie movie, String userRole) {
        if (!userRole.equals("ROLE_USER") && !userRole.equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("Only logged-in users can add or update movies.");
        }
        return movieRepository.save(movie);
    }

    // Delete a movie (restricted to admins)
    public void deleteMovie(Long id, String userRole) {
        if (!userRole.equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("Only admins can delete movies.");
        }
        movieRepository.deleteById(id);
    }

    // Fetch movies from an external API (restricted to logged-in users)
    public List<Movie> fetchMoviesFromExternalApi(String userRole) {
        if (!userRole.equals("ROLE_USER") && !userRole.equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("Only logged-in users can fetch movies from external APIs.");
        }

        MovieApiResponse response = restTemplate.getForObject(EXTERNAL_API_URL, MovieApiResponse.class);
        if (response != null) {
            List<Movie> movies = response.getResults();
            for (Movie movie : movies) {
                movieRepository.save(movie);
            }
            return movies;
        } else {
            throw new RuntimeException("Failed to fetch movies from external API.");
        }
    }
}