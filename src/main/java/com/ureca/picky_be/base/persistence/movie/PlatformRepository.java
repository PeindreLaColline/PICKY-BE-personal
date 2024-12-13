package com.ureca.picky_be.base.persistence.movie;

import com.ureca.picky_be.jpa.movie.Movie;
import com.ureca.picky_be.jpa.platform.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {
    void deleteAllByMovie(Movie movie);
    List<Platform> findAllByMovie(Movie movie);
}
