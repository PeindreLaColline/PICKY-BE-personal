package com.ureca.picky_be.base.business.movie.dto;

import java.util.List;

public record GetRecommendMovieResp(Long movieId,
                                    String title,
                                    Double totalRating,
                                    String posterUrl,
                                    List<GetGenres> genres,
                                    List<GetPlatform> platforms) {
}
