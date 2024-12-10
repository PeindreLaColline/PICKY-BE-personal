package com.ureca.picky_be.base.persistence.movie;

import com.ureca.picky_be.jpa.movie.MovieLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;

@Repository
public interface MovieLikeRepository extends JpaRepository<MovieLike, Long> {

    @Query("SELECT ml FROM MovieLike ml WHERE ml.movie.id = :movieId AND ml.user.id = :userId")
    Optional<MovieLike> findByMovieIdAndUserId(@Param("movieId") Long movieId, @Param("userId") Long userId);

    @Modifying(clearAutomatically = true)
    @Transactional
    void deleteByUserId(Long userId);

    boolean existsByMovieIdAndUserId(@Param("movieId") Long movieId, @Param("userId") Long userId);

    @Query("SELECT ml.user.id FROM MovieLike ml " +
            "WHERE ml.movie.id = :movieId AND ml.user.status = 'REGULAR'")
    List<Long> findUserIdsByMovieId(@Param("movieId") Long movieId);
}
