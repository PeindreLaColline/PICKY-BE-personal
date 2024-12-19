package com.ureca.picky_be.base.business.movie.dto;

import java.time.LocalDateTime;

//플레이리스트 반환
public interface GetSimpleMovieProjection {
    Long getPlaylistId();
    Long getMovieId();
    String getTitle();
    Integer getLikes();
    Integer getLineReviews();
    LocalDateTime getCreatedAt();
    double getTotalRating();
    String getPosterUrl();
    String getBackdropUrl();
}