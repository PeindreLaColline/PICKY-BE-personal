package com.ureca.picky_be.base.business.movie;

import com.ureca.picky_be.base.business.movie.dto.*;
import com.ureca.picky_be.base.business.user.dto.GetMoviesForRegisReq;
import com.ureca.picky_be.base.business.user.dto.GetMoviesForRegisResp;
import com.ureca.picky_be.base.implementation.auth.AuthManager;
import com.ureca.picky_be.base.implementation.mapper.MovieDtoMapper;
import com.ureca.picky_be.base.implementation.movie.MovieManager;
import com.ureca.picky_be.base.implementation.user.UserManager;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.genre.Genre;
import com.ureca.picky_be.jpa.movie.FilmCrew;
import com.ureca.picky_be.jpa.movie.Movie;
import com.ureca.picky_be.jpa.movie.MovieBehindVideo;
import com.ureca.picky_be.jpa.platform.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService implements MovieUseCase{
    private final MovieManager movieManager;
    private final MovieDtoMapper movieDtoMapper;
    private final AuthManager authManager;
    private final UserManager userManager;

    @Override
    public List<GetMoviesForRegisResp> getMoviesByGenre(GetMoviesForRegisReq getMoviesForRegisReq) {
        return movieManager.getMoviesByGenres(getMoviesForRegisReq);
    }

    @Override
    public SuccessCode addMovie(AddMovieReq addMovieReq) {
        Movie movie = movieManager.addMovie(addMovieReq);
        return movieManager.addStreamingPlatform(addMovieReq, movie);
    }

    @Override
    public GetMovieDetailResp getMovieDetail(Long movieId) {
        Movie movie = movieManager.getMovie(movieId);
        List<MovieBehindVideo> movieBehindVideos = movieManager.getMovieBehindVideos(movieId);
        List<Genre> genres = movieManager.getGenre(movieId);
        List<FilmCrew> actors = movieManager.getActors(movie);
        List<FilmCrew> directors = movieManager.getDirectors(movie);
        boolean like = movieManager.getMovieLike(movieId, authManager.getUserId());
        List<Platform> platforms = movieManager.getStreamingPlatform(movie);
        return movieDtoMapper.toGetMovieDetailResp(movie, movieBehindVideos, genres, actors, directors, like, platforms, movie.getTotalRating());
    }

    @Override
    public SuccessCode updateMovie(Long movieId, UpdateMovieReq updateMovieReq) {
        return movieManager.updateMovie(movieId, updateMovieReq);
    }

    @Override
    public List<GetSimpleMovieResp> getRecommends() {
        List<Long> movieIds = movieManager.getRecommendsFromAi(authManager.getUserId());

        List<Movie> movies = movieIds.stream()
                .map(movieId -> {
                    if (!movieManager.isExists(movieId)) {
                        return movieManager.saveMovieAuto(movieId);
                    } else {
                        return movieManager.findByMovieId(movieId);
                    }
                })
                .collect(Collectors.toList());

        List<GetSimpleMovieProjection> simpleMovieProjections = movieManager.getRecommendsAi(movies);
        return movieDtoMapper.toGetSimpleMovies(simpleMovieProjections).toList();
    }

    @Override
    public List<GetSimpleMovieResp> getTop10() {
        return movieManager.getTop10();
    }

    @Override
    public Slice<GetSimpleMovieResp> getMoviesByGenre(Long genreId, Long lastMovieId, Integer lastLikeCount) {
        return movieManager.getMoviesByGenre(genreId, lastMovieId, lastLikeCount);
    }

    @Override
    public boolean movieLike(Long movieId){
        return movieManager.movieLike(movieId, authManager.getUserId());
    }

    @Override
    public List<GetGenres> getGenres(){
        List<Genre> genres = movieManager.getGenres();
        return movieDtoMapper.toGetGenres(genres);
    }

    @Override
    public Slice<GetSimpleMovieResp> getMoviesOrderByCreatedAt(Long lastMovieId, LocalDateTime createdAt, int size) {
        List<GetSimpleMovieProjection> simpleMovieProjections = movieManager.getMoviesOrderByCreatedAt(lastMovieId, createdAt, size);
        return movieDtoMapper.toGetSimpleMovies(simpleMovieProjections);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<GetUserLikeMovieResp> getUserLikeMoviesByNickname(PageRequest pageRequest, GetUserLikeMovieReq req) {
        Long userId = userManager.getUserIdByNickname(req.nickname());

        Slice<GetUserLikeMovieResp> likeMovies = movieManager.findLikeMoviesByNickname(userId, req, pageRequest);
        return likeMovies;
    }
}
