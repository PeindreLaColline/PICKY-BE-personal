package com.ureca.picky_be.base.persistence.movie;

import com.ureca.picky_be.jpa.entity.movie.MovieBehindVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieBehindVideoRepository extends JpaRepository<MovieBehindVideo, Long> {
    List<MovieBehindVideo> findAllByMovieId(Long movieId);
    void deleteAllByMovieId(Long movieId);
}
