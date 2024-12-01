package com.ureca.picky_be.base.persistence.movie;

import com.ureca.picky_be.jpa.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findTop45ByOrderByTotalRatingDesc();
}