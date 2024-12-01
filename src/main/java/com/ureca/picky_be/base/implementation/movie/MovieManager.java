package com.ureca.picky_be.base.implementation.movie;

import com.ureca.picky_be.base.persistence.movie.MovieRepository;
import com.ureca.picky_be.jpa.movie.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MovieManager {
    private final MovieRepository movieRepository;

    public List<Movie> getMovieListForPreference(){
        return movieRepository.findTop45ByOrderByTotalRatingDesc();
    }
}
