package com.ureca.picky_be.base.implementation.mapper;

import com.ureca.picky_be.base.business.movie.dto.MoviePreferenceResp;
import com.ureca.picky_be.jpa.movie.Movie;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieDtoMapper {

    public List<MoviePreferenceResp> toMoviePreferenceResp(List<Movie> movies) {
        return movies.stream()
                .map(movie -> new MoviePreferenceResp(movie.getId(), movie.getPosterUrl()))
                .toList();
    }
}