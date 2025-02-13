package com.ureca.picky_be.base.presentation;

import com.ureca.picky_be.base.business.movie.dto.AddMovieAuto;
import com.ureca.picky_be.base.implementation.movie.MovieManager;
import com.ureca.picky_be.jpa.entity.genre.Genre;
import com.ureca.picky_be.jpa.entity.movie.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dummy")
public class DummyController {

    private final MovieManager movieManager;

    @PostMapping
    public void addDummyMoive(@RequestParam Long newMovie) {
        for(Long movieId=newMovie; movieId<=50; movieId++) {
            AddMovieAuto addMovieAuto = movieManager.saveMovieAuto(movieId);
            if(addMovieAuto == null
                    || addMovieAuto.genres() == null || addMovieAuto.genres().isEmpty()
                    || addMovieAuto.releaseDate() == null) {
                continue;
            }
            Movie movie = movieManager.addMovieAuto(addMovieAuto);
            movieManager.addMovieGenresAuto(addMovieAuto.genres(), movie);
            movieManager.addActorsAuto(addMovieAuto.credits(), movie);
            movieManager.addDirectorsAuto(addMovieAuto.credits(), movie);
            List<Genre> genres = movieManager.getGenre(movieId);
            //movieManager.addMovieElastic(movie, genres);
        }
    }
}
