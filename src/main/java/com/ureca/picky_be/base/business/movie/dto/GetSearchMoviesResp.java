package com.ureca.picky_be.base.business.movie.dto;

import java.util.Date;

public record GetSearchMoviesResp(
        Long movieId,
        String movieTitle,
        String moviePosterUrl,
        Date releaseDate
) {

}
