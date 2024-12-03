package com.ureca.picky_be.base.persistence.movie;

import com.ureca.picky_be.base.business.movie.dto.GetSimpleMovieResp;
import com.ureca.picky_be.jpa.genre.Genre;
import com.ureca.picky_be.jpa.movie.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findTop45ByOrderByTotalRatingDesc();

    @Query("""
    SELECT new com.ureca.picky_be.base.business.movie.dto.GetSimpleMovieResp(
        m.id,
        m.title,
        CAST(COUNT(ml) AS int),
        m.totalRating,
        m.posterUrl,
        m.backdropUrl
    )
    FROM Movie m
    LEFT JOIN MovieLike ml ON ml.movie.id = m.id
    GROUP BY m.id, m.title, m.totalRating, m.posterUrl, m.backdropUrl
    ORDER BY m.totalRating DESC
""")
    List<GetSimpleMovieResp> findTop30MoviesWithLikes(Pageable pageable);

    @Query("""
    SELECT new com.ureca.picky_be.base.business.movie.dto.GetSimpleMovieResp(
        m.id,
        m.title,
        CAST(COUNT(ml) AS int),
        m.totalRating,
        m.posterUrl,
        m.backdropUrl
    )
    FROM Movie m
    LEFT JOIN MovieLike ml ON ml.movie.id = m.id
    GROUP BY m.id, m.title, m.totalRating, m.posterUrl, m.backdropUrl
    ORDER BY m.totalRating DESC
""")
    List<GetSimpleMovieResp> findTop10MoviesWithLikes(Pageable pageable);

    @Query("""
    SELECT new com.ureca.picky_be.base.business.movie.dto.GetSimpleMovieResp(
        m.id,
        m.title,
        CAST(COUNT(ml) AS int),
        m.totalRating,
        m.posterUrl,
        m.backdropUrl
    )
    FROM Movie m
    JOIN MovieGenre mg ON mg.movieId = m
    LEFT JOIN MovieLike ml ON ml.movie.id = m.id
    WHERE mg.genreId = :genreId
      AND (:lastMovieId IS NULL OR m.id < :lastMovieId)
    GROUP BY m.id, m.title, m.totalRating, m.posterUrl, m.backdropUrl
    ORDER BY m.totalRating DESC, m.createdAt DESC
""")
    List<GetSimpleMovieResp> findMoviesByGenreIdWithLikes(
            @Param("genreId") Long genreId,
            @Param("lastMovieId") Long lastMovieId,
            Pageable pageable
    );
}

