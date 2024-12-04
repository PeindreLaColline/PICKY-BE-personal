package com.ureca.picky_be.base.business.movie.dto;

public record GetSimpleMovieResp(
        Long movieId,
        String title,
        Integer likes,
        double totalRating,
        String posterUrl,
        String backdropUrl
) {
}
