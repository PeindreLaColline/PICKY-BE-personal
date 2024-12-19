package com.ureca.picky_be.base.business.movie.dto;

import java.util.Date;
import java.util.List;

public record GetSearchMoviesResp(
        Long movieId,
        String movieTitle,
        String moviePosterUrl,
        Date releaseDate,
        List<GetGenres> genres,
        String originalLanguage
) {
}
