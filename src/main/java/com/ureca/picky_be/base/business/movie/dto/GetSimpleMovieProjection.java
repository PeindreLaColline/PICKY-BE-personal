package com.ureca.picky_be.base.business.movie.dto;

//플레이리스트 반환
public interface GetSimpleMovieProjection {
    Long getPlaylistId();
    Long getMovieId();
    String getTitle();
    Integer getLikes();
    double getTotalRating();
    String getPosterUrl();
    String getBackdropUrl();
}