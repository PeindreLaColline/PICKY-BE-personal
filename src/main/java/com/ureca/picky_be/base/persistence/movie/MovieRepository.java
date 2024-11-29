package com.ureca.picky_be.base.persistence.movie;

import com.ureca.picky_be.jpa.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {

}
