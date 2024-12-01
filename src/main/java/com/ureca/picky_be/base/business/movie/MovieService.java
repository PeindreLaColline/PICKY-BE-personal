package com.ureca.picky_be.base.business.movie;

import com.ureca.picky_be.base.business.movie.dto.MoviePreferenceResp;
import com.ureca.picky_be.base.implementation.mapper.MovieDtoMapper;
import com.ureca.picky_be.base.implementation.movie.MovieManager;
import com.ureca.picky_be.jpa.movie.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService implements MovieUseCase{
    private final MovieManager movieManager;
    private final MovieDtoMapper movieDtoMapper;

    @Override
    public List<MoviePreferenceResp> getMovieListForPreference() {
        List<Movie> movies = movieManager.getMovieListForPreference();
        return movieDtoMapper.toMoviePreferenceResp(movies);
    }
}
