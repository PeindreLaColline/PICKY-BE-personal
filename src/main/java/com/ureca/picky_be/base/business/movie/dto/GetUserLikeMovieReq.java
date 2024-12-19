package com.ureca.picky_be.base.business.movie.dto;

public record GetUserLikeMovieReq(
        String nickname,
        Long lastMovieLikeId
) {
}
