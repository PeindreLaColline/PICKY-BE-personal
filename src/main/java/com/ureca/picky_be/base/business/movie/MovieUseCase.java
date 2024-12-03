package com.ureca.picky_be.base.business.movie;

import com.ureca.picky_be.base.business.movie.dto.*;
import com.ureca.picky_be.global.success.SuccessCode;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface MovieUseCase {
    SuccessCode addMovie(AddMovieReq addMovieReq);
    GetMovieDetailResp getMovieDetail(Long movieId);
    List<MoviePreferenceResp> getMovieListForPreference();
    SuccessCode updateMovie(Long movieId, UpdateMovieReq updateMovieReq);
    List<GetSimpleMovieResp> getRecommends();
    List<GetSimpleMovieResp> getTop10();
    List<GetSimpleMovieResp> getMoviesByGenre(Long genreId, Long lastMovieId, Integer lastLikeCount);
    boolean movieLike(Long movieId);
}
