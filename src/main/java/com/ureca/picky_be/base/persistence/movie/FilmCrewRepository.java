package com.ureca.picky_be.base.persistence.movie;

import com.ureca.picky_be.jpa.movie.FilmCrew;
import com.ureca.picky_be.jpa.movie.FilmCrewPosition;
import com.ureca.picky_be.jpa.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmCrewRepository extends JpaRepository<FilmCrew, Integer> {
    List<FilmCrew> findByMovieIdAndFilmCrewPosition(Movie movie, FilmCrewPosition filmCrewPosition);
}
