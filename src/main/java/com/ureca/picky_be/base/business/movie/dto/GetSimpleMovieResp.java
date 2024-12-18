package com.ureca.picky_be.base.business.movie.dto;

import java.time.LocalDateTime;

public record GetSimpleMovieResp(
        Long movieId,
        String title,
        Integer likes,
        LocalDateTime createdAt,
        double totalRating,
        String posterUrl,
        String backdropUrl
) {
}
