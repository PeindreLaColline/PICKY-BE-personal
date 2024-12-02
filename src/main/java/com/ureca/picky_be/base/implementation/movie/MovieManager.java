package com.ureca.picky_be.base.implementation.movie;

import com.ureca.picky_be.base.business.movie.dto.AddMovieReq;
import com.ureca.picky_be.base.business.movie.dto.GetMovieDetailResp;
import com.ureca.picky_be.base.persistence.movie.*;
import com.ureca.picky_be.base.persistence.movieworker.MovieWorkerRepository;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.genre.Genre;
import com.ureca.picky_be.jpa.movie.*;
import com.ureca.picky_be.jpa.movieworker.MovieWorker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MovieManager {
    private final MovieRepository movieRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final MovieBehindVideoRepository movieBehindVideoRepository;
    private final MovieWorkerRepository movieWorkerRepository;
    private final FilmCrewRepository filmCrewRepository;
    private final GenreRepository genreRepository;

    public List<Movie> getMovieListForPreference(){
        return movieRepository.findTop45ByOrderByTotalRatingDesc();
    }

    public SuccessCode addMovie(AddMovieReq addMovieReq) {
        Movie movie = addMovieInfo(addMovieReq);
        List<MovieBehindVideo> movieBehindVideos = addMovieBehindVideos(addMovieReq.movieBehindVideos(), movie);
        List<MovieGenre> movieGenres = addMovieGenres(addMovieReq.movieInfo().genres(), movie);
        List<FilmCrew> actors = addActors(addMovieReq.movieInfo().credits(), movie);
        List<FilmCrew> directors = addDirectors(addMovieReq.movieInfo().credits(), movie);
        return SuccessCode.CREATE_MOVIE_SUCCESS;
    }

    private Movie addMovieInfo(AddMovieReq addMovieReq) {
        Movie movie = Movie.builder()
                .id(addMovieReq.movieInfo().id())
                .title(addMovieReq.movieInfo().title())
                .releaseDate(addMovieReq.movieInfo().releaseDate())
                .posterUrl(addMovieReq.movieInfo().posterUrl())
                .totalRating(2.5)
                .plot(addMovieReq.movieInfo().plot())
                .runningTime(addMovieReq.movieInfo().runtime())
                .trailerUrl(addMovieReq.trailer())
                .ostUrl(addMovieReq.ost())
                .build();
        return movieRepository.save(movie);
    }

    private List<MovieBehindVideo> addMovieBehindVideos(List<String> movieBehindVideosReq, Movie movie) {
        List<MovieBehindVideo> movieBehindVideos = movieBehindVideosReq.stream()
                .map(url -> MovieBehindVideo.builder()
                        .url(url)
                        .movie(movie)
                        .build())
                .toList();
        return movieBehindVideoRepository.saveAll(movieBehindVideos);
    }

    private List<MovieGenre> addMovieGenres(List<AddMovieReq.MovieInfo.GenreInfo> genres, Movie movie) {
        List<MovieGenre> movieGenres = genres.stream()
                .map(genre -> MovieGenre.builder()
                        .movieId(movie)
                        .genreId(genre.id())
                        .build())
                .toList();
        return movieGenreRepository.saveAll(movieGenres);
    }

    private List<FilmCrew> addActors(AddMovieReq.MovieInfo.Credits credits, Movie movie) {
        List<FilmCrew> filmCrews = credits.cast().stream()
                .limit(10)
                .map(cast -> {
                    MovieWorker movieWorker = movieWorkerRepository.findById(cast.id())
                            .orElseGet(() -> movieWorkerRepository.save(
                                    MovieWorker.builder()
                                            .id(cast.id())
                                            .name(cast.name())
                                            .profileUrl(cast.profileUrl())
                                            .build()
                            ));

                    return FilmCrew.builder()
                            .movieWorkerId(movieWorker)
                            .movieId(movie)
                            .role(cast.role())
                            .filmCrewPosition(FilmCrewPosition.ACTOR)
                            .build();
                })
                .toList();
        return filmCrewRepository.saveAll(filmCrews);
    }

    private List<FilmCrew> addDirectors(AddMovieReq.MovieInfo.Credits credits, Movie movie) {
        List<FilmCrew> filmCrews = credits.getDirectingCrew().stream()
                .limit(10)
                .map(cast -> {
                    MovieWorker movieWorker = movieWorkerRepository.findById(cast.id())
                            .orElseGet(() -> movieWorkerRepository.save(
                                    MovieWorker.builder()
                                            .id(cast.id())
                                            .name(cast.name())
                                            .profileUrl(cast.profileUrl())
                                            .build()
                            ));

                    return FilmCrew.builder()
                            .movieWorkerId(movieWorker)
                            .movieId(movie)
                            .role("감독감독~!")
                            .filmCrewPosition(FilmCrewPosition.DIRECTOR)
                            .build();
                })
                .toList();
        return filmCrewRepository.saveAll(filmCrews);
    }

    public Movie getMovie(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));
    }

    public List<MovieBehindVideo> getMovieBehindVideos(Long movieId) {
        return movieBehindVideoRepository.findAllByMovieId(movieId);
    }

    public List<Genre> getGenre(Long movieId) {
        List<Long> genreIds = movieGenreRepository.getGenreIdsByMovieId(movieId);
        return genreRepository.findAllByIdIn(genreIds);
    }

    public List<FilmCrew> getActors(Movie movie) {
        return filmCrewRepository.findByMovieIdAndFilmCrewPosition(movie, FilmCrewPosition.ACTOR);
    }

    public List<FilmCrew> getDirectors(Movie movie) {
        return filmCrewRepository.findByMovieIdAndFilmCrewPosition(movie, FilmCrewPosition.DIRECTOR);
    }
}
