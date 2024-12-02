package com.ureca.picky_be.base.business.movie;

import com.ureca.picky_be.base.business.movie.dto.AddMovieReq;
import com.ureca.picky_be.base.business.movie.dto.GetMovieDetailResp;
import com.ureca.picky_be.base.business.movie.dto.MoviePreferenceResp;
import com.ureca.picky_be.global.success.SuccessCode;

import java.util.List;

public interface MovieUseCase {
    SuccessCode addMovie(AddMovieReq addMovieReq);
    GetMovieDetailResp getMovieDetail(Long movieId);
    List<MoviePreferenceResp> getMovieListForPreference();
}
