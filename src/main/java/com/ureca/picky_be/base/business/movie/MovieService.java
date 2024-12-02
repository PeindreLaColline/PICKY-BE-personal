package com.ureca.picky_be.base.business.movie;

import com.ureca.picky_be.base.business.movie.dto.AddMovieReq;
import com.ureca.picky_be.base.business.movie.dto.GetMovieDetailResp;
import com.ureca.picky_be.base.business.movie.dto.MoviePreferenceResp;
import com.ureca.picky_be.base.business.movie.dto.UpdateMovieReq;
import com.ureca.picky_be.base.implementation.auth.AuthManager;
import com.ureca.picky_be.base.implementation.mapper.MovieDtoMapper;
import com.ureca.picky_be.base.implementation.movie.MovieManager;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.genre.Genre;
import com.ureca.picky_be.jpa.movie.FilmCrew;
import com.ureca.picky_be.jpa.movie.Movie;
import com.ureca.picky_be.jpa.movie.MovieBehindVideo;
import com.ureca.picky_be.jpa.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService implements MovieUseCase{
    private final MovieManager movieManager;
    private final MovieDtoMapper movieDtoMapper;
    private final AuthManager authManager;

    @Override
    public List<MoviePreferenceResp> getMovieListForPreference() {
        List<Movie> movies = movieManager.getMovieListForPreference();
        return movieDtoMapper.toMoviePreferenceResp(movies);
    }

    @Override
    public SuccessCode addMovie(AddMovieReq addMovieReq) {
        // 개발 편의를 위해 관리자 권한 체크 임시 주석 처리
        /*if(authManager.getUserRole() != Role.ADMIN){
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }*/
        return movieManager.addMovie(addMovieReq);
    }

    @Override
    public GetMovieDetailResp getMovieDetail(Long movieId) {
        Movie movie = movieManager.getMovie(movieId);
        List<MovieBehindVideo> movieBehindVideos = movieManager.getMovieBehindVideos(movieId);
        List<Genre> genres = movieManager.getGenre(movieId);
        List<FilmCrew> actors = movieManager.getActors(movie);
        List<FilmCrew> directors = movieManager.getDirectors(movie);
        return movieDtoMapper.toGetMovieDetailResp(movie, movieBehindVideos, genres, actors, directors);
    }

    @Override
    public SuccessCode updateMovie(Long movieId, UpdateMovieReq updateMovieReq) {
        // 개발 편의를 위해 관리자 권한 체크 임시 주석 처리
        /*if(authManager.getUserRole() != Role.ADMIN){
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }*/
        return movieManager.updateMovie(movieId, updateMovieReq);
    }
}
