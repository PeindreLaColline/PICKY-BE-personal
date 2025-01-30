package com.ureca.picky_be.base.persistence.movie;

import com.ureca.picky_be.base.business.movie.dto.*;
import com.ureca.picky_be.base.business.user.dto.GetMoviesForRegisResp;
import com.ureca.picky_be.jpa.entity.movie.Movie;
import jakarta.persistence.Tuple;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("""
    SELECT new com.ureca.picky_be.base.business.user.dto.GetMoviesForRegisResp(
        m.id,
        m.title,
        m.posterUrl
    )
    FROM Movie m
    JOIN MovieGenre mg ON mg.movie = m
    WHERE mg.genreId IN :genreIds
    GROUP BY m.id, m.posterUrl, m.totalRating, m.createdAt
    ORDER BY m.totalRating DESC, m.createdAt DESC
""")
    List<GetMoviesForRegisResp> findMovieByGenresOrderByTotalRating(@Param("genreIds") List<Long> genreIds, Pageable pageable);

    @Query("""
    SELECT new com.ureca.picky_be.base.business.movie.dto.GetSimpleMovieResp(
        m.id,
        m.title,
        CAST(COUNT(ml) AS int),
        CAST(COUNT(lr) AS int),
        m.createdAt,
        m.totalRating,
        m.posterUrl,
        m.backdropUrl
    )
    FROM Movie m
    LEFT JOIN MovieLike ml ON ml.movie.id = m.id
    LEFT JOIN LineReview lr ON lr.movieId = m.id
    GROUP BY m.id, m.title, m.createdAt, m.totalRating, m.posterUrl, m.backdropUrl
    ORDER BY m.totalRating DESC
""")
    List<GetSimpleMovieResp> findTop30MoviesWithLikes(Pageable pageable);

    @Query("""
    SELECT new com.ureca.picky_be.base.business.movie.dto.GetSimpleMovieResp(
        m.id,
        m.title,
        CAST((SELECT COUNT(ml) FROM MovieLike ml WHERE ml.movie.id = m.id) AS int),
        CAST((SELECT COUNT(lr) FROM LineReview lr WHERE lr.movieId = m.id) AS int),
        m.createdAt,
        m.totalRating,
        m.posterUrl,
        m.backdropUrl
    )
    FROM Movie m
    LEFT JOIN MovieLike ml ON ml.movie.id = m.id
    LEFT JOIN LineReview lr ON lr.movieId = m.id
    GROUP BY m.id, m.title, m.createdAt, m.totalRating, m.posterUrl, m.backdropUrl
    ORDER BY m.popularity DESC
""")
    List<GetSimpleMovieResp> findTop10MoviesWithLikes(Pageable pageable);

    @Query("""
    SELECT new com.ureca.picky_be.base.business.movie.dto.GetSimpleMovieResp(
        m.id,
        m.title,
        CAST(COUNT(ml) AS int),
        CAST(COUNT(lr) AS int),
        m.createdAt,
        m.totalRating,
        m.posterUrl,
        m.backdropUrl
    )
    FROM Movie m
    JOIN MovieGenre mg ON mg.movie = m
    LEFT JOIN MovieLike ml ON ml.movie.id = m.id
    LEFT JOIN LineReview lr ON lr.movieId = m.id
    WHERE mg.genreId = :genreId
    GROUP BY m.id, m.title, m.createdAt, m.totalRating, m.posterUrl, m.backdropUrl
    HAVING (
        :lastMovieId IS NULL AND :createdAt IS NULL
        OR (m.createdAt < :createdAt)
    )
    ORDER BY m.popularity DESC, m.createdAt DESC
""")
    Slice<GetSimpleMovieResp> findMoviesByGenreIdWithLikesUsingCursor(
            @Param("genreId") Long genreId,
            @Param("createdAt") LocalDateTime createdAt,
            @Param("lastMovieId") Long lastMovieId,
            Pageable pageable
    );

    @Query(value = """
    SELECT
        mpl.playlist.id AS playlistId,
        m.id AS movieId,
        m.title AS title,
        CAST(COUNT(ml) AS int) AS likes,
        CAST(COUNT(lr) AS int) AS lineReviews,
        m.totalRating AS totalRating,
        m.posterUrl AS posterUrl,
        m.backdropUrl AS backdropUrl
    FROM Movie m
    JOIN MoviePlaylist mpl ON mpl.movie.id = m.id
    LEFT JOIN MovieLike ml ON ml.movie.id = m.id
    LEFT JOIN LineReview lr ON lr.movieId = m.id
    WHERE mpl.playlist.id IN :playlistIds
    GROUP BY mpl.playlist.id, m.id, m.title, m.totalRating, m.posterUrl, m.backdropUrl
    ORDER BY m.id DESC
    """)
    List<GetSimpleMovieProjection> getMoviesByPlaylistIds(@Param("playlistIds") List<Long> playlistIds);

    @Query("""
    SELECT
        m.id AS movieId,
        m.title AS title,
        CAST(COUNT(ml) AS int) AS likes,
        CAST(COUNT(lr) AS int) AS lineReviews,
        m.totalRating AS totalRating,
        m.posterUrl AS posterUrl,
        m.backdropUrl AS backdropUrl
    FROM Movie m
    LEFT JOIN MovieLike ml ON ml.movie.id = m.id
    LEFT JOIN LineReview lr ON lr.movieId = m.id
    WHERE (:lastMovieId IS NULL OR m.id < :lastMovieId)
    GROUP BY m.id, m.title, m.totalRating, m.posterUrl, m.backdropUrl
    ORDER BY m.id DESC, :createdAt DESC
    """)
    List<GetSimpleMovieProjection> findMoviesOrderByCreatedAtUsingCursor(Long lastMovieId, LocalDateTime createdAt, Pageable pageable);

    @Query("""
    SELECT DISTINCT m.id AS movieId,
           m.title AS title,
           m.totalRating AS totalRating,
           m.posterUrl AS posterUrl,
           g.id AS genreId,
           g.name AS genreName,
           p.id AS platformId,
           p.platformType AS platformName,
           p.url AS platformUrl
    FROM Movie m
    LEFT JOIN MovieGenre mg ON mg.movie.id = m.id
    LEFT JOIN Genre g ON g.id = mg.genreId
    LEFT JOIN Platform p ON p.movie.id = m.id
    WHERE m.id IN :movieIds
    ORDER BY m.popularity DESC
""")
    List<GetRecommendMovieProjection> findMoviesWithGenresAndPlatforms(@Param("movieIds") List<Long> movieIds);

    @Query(value = """
    SELECT 
        distinct m.id AS movieId,
        m.title AS movieTitle,
        m.poster_url AS moviePosterUrl,
        m.release_date AS releaseDate,
        m.original_language AS originalLanguage
    FROM movie m
    WHERE MATCH(m.title) AGAINST(:keyword IN BOOLEAN MODE)
""", nativeQuery = true)
    List<Tuple> findSearchMoviesMySQL(@Param("keyword") String keyword);

}

