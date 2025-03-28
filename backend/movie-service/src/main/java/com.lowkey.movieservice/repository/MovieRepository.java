package com.lowkey.movieservice.repository;

import com.lowkey.movieservice.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Find movies by title (case-insensitive search)
    List<Movie> findByTitleIgnoreCase(String title);

    // Find movies by genre
    List<Movie> findByGenre(String genre);

    // Custom query for top-rated movies (example use case)
    List<Movie> findByRatingGreaterThan(Double rating);
}