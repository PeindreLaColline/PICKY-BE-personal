package com.ureca.picky_be.base.implementation.mapper;

import com.ureca.picky_be.base.business.movie.dto.GetMovieDetailResp;
import com.ureca.picky_be.base.business.movie.dto.MoviePreferenceResp;
import com.ureca.picky_be.jpa.genre.Genre;
import com.ureca.picky_be.jpa.movie.FilmCrew;
import com.ureca.picky_be.jpa.movie.Movie;
import com.ureca.picky_be.jpa.movie.MovieBehindVideo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MovieDtoMapper {

    public GetMovieDetailResp toGetMovieDetailResp(
            Movie movie,
            List<MovieBehindVideo> movieBehindVideos,
            List<Genre> genres,
            List<FilmCrew> actors,
            List<FilmCrew> directors
    ) {
        List<GetMovieDetailResp.MovieInfo.GenreInfo> genreInfoList = genres.stream()
                .map(genre -> new GetMovieDetailResp.MovieInfo.GenreInfo(genre.getId()))
                .toList();

        List<GetMovieDetailResp.MovieInfo.Credits.Cast> castList = actors.stream()
                .map(actor -> new GetMovieDetailResp.MovieInfo.Credits.Cast(
                        actor.getMovieWorkerId().getId(),
                        actor.getRole(),
                        actor.getMovieWorkerId().getName(),
                        actor.getMovieWorkerId().getProfileUrl()
                ))
                .toList();

        List<GetMovieDetailResp.MovieInfo.Credits.Crew> crewList = directors.stream()
                .map(director -> new GetMovieDetailResp.MovieInfo.Credits.Crew(
                        director.getMovieWorkerId().getId(),
                        "Director",
                        director.getMovieWorkerId().getName(),
                        director.getMovieWorkerId().getProfileUrl()
                ))
                .toList();

        List<String> movieBehindVideoUrls = movieBehindVideos.stream()
                .map(MovieBehindVideo::getUrl)
                .toList();

        return new GetMovieDetailResp(
                new GetMovieDetailResp.MovieInfo(
                        movie.getId(),
                        movie.getTitle(),
                        movie.getReleaseDate(),
                        movie.getPosterUrl(),
                        movie.getPlot(),
                        movie.getRunningTime(),
                        genreInfoList,
                        new GetMovieDetailResp.MovieInfo.Credits(castList, crewList)
                ),
                Optional.ofNullable(movie.getTrailerUrl()).orElse("Trailer not found"),
                Optional.ofNullable(movie.getOstUrl()).orElse("OST not found"),
                movieBehindVideoUrls
        );
    }

    public List<MoviePreferenceResp> toMoviePreferenceResp(List<Movie> movies) {
        return movies.stream()
                .map(movie -> new MoviePreferenceResp(movie.getId(), movie.getPosterUrl()))
                .toList();
    }
}