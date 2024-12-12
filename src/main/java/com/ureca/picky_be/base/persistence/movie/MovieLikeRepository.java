package com.ureca.picky_be.base.persistence.movie;

import com.ureca.picky_be.base.business.movie.dto.GetUserLikeMovieResp;
import com.ureca.picky_be.jpa.movie.MovieLike;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
            "WHERE ml.movie.id = :movieId")
    List<Long> findUserIdsByMovieId(@Param("movieId") Long movieId);

//    SELECT ml.id AS id, m.id AS movieId, m.title AS movieTitle, m.posterUrl AS moviePosterUrl, m.totalRating AS rating
    @Query("""
        SELECT new com.ureca.picky_be.base.business.movie.dto.GetUserLikeMovieResp(
                    ml.id, m.id, m.title, m.posterUrl, m.totalRating
        )
        FROM MovieLike ml
        LEFT JOIN Movie m ON ml.movie = m
        WHERE ml.user.id = :userId AND (:lastMovieLikeId IS NULL OR ml.id < :lastMovieLikeId)
        
        ORDER BY ml.createdAt DESC
    """)
    Slice<GetUserLikeMovieResp> findByUserId(
            @Param("userId") Long userId,
            @Param("lastMovieLikeId") Long lastMovieLikeId,
            Pageable pageable
    );
}
