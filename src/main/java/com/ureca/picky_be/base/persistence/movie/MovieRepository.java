package com.ureca.picky_be.base.persistence.movie;

import com.ureca.picky_be.jpa.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findTop45ByOrderByTotalRatingDesc();
}