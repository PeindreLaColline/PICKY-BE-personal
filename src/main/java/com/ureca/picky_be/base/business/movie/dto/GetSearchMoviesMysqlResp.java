package com.ureca.picky_be.base.business.movie.dto;

import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

public record GetSearchMoviesMysqlResp(
        Long movieId,
        String movieTitle,
        String moviePosterUrl,
        Date releaseDate,
        String originalLanguage
) {
}
