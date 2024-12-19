package com.ureca.picky_be.base.business.movie.dto;

public record GetUserLikeMovieResp(
        Long movieLikeId,
        Long movieId,
        String movieTitle,
        String moviePosterUrl,
        double movieTotalRating
) {
}
