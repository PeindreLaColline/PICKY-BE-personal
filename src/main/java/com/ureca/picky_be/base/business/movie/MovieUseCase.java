package com.ureca.picky_be.base.business.movie;

import com.ureca.picky_be.base.business.movie.dto.MoviePreferenceResp;

import java.util.List;

public interface MovieUseCase {
    List<MoviePreferenceResp> getMovieListForPreference();
}
