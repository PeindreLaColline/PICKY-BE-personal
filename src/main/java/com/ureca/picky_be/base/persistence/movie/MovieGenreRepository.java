package com.ureca.picky_be.base.persistence.movie;

import com.ureca.picky_be.jpa.movie.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieGenreRepository extends JpaRepository<MovieGenre, Long> {
    @Query("SELECT mg.genreId FROM MovieGenre mg WHERE mg.movieId.id = :movieId")
    List<Long> getGenreIdsByMovieId(@Param("movieId") Long movieId);

}
