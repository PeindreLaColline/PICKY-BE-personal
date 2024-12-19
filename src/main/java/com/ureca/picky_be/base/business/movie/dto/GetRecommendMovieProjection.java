package com.ureca.picky_be.base.business.movie.dto;

public interface GetRecommendMovieProjection {
    Long getMovieId();
    String getTitle();
    Double getTotalRating();
    String getPosterUrl();
    Long getGenreId();
    String getGenreName();
    Long getPlatformId();
    String getPlatformName();
    String getPlatformUrl();
}
