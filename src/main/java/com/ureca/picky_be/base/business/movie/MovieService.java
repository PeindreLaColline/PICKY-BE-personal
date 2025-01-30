package com.ureca.picky_be.base.business.movie;

import com.ureca.picky_be.base.business.movie.dto.*;
import com.ureca.picky_be.base.business.user.dto.GetMoviesForRegisReq;
import com.ureca.picky_be.base.business.user.dto.GetMoviesForRegisResp;
import com.ureca.picky_be.base.implementation.auth.AuthManager;
import com.ureca.picky_be.base.implementation.lineReview.LineReviewManager;
import com.ureca.picky_be.base.implementation.mapper.MovieDtoMapper;
import com.ureca.picky_be.base.implementation.movie.MovieManager;
import com.ureca.picky_be.base.implementation.user.UserManager;
import com.ureca.picky_be.base.persistence.movie.MovieRepository;
import com.ureca.picky_be.elasticsearch.document.movie.MovieDocument;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.entity.genre.Genre;
import com.ureca.picky_be.jpa.entity.lineReview.LineReview;
import com.ureca.picky_be.jpa.entity.movie.FilmCrew;
import com.ureca.picky_be.jpa.entity.movie.Movie;
import com.ureca.picky_be.jpa.entity.movie.MovieBehindVideo;
import com.ureca.picky_be.jpa.entity.platform.Platform;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
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
    private final LineReviewManager lineReviewManager;

    @Override
    public List<GetMoviesForRegisResp> getMoviesByGenre(GetMoviesForRegisReq getMoviesForRegisReq) {
        return movieManager.getMoviesByGenres(getMoviesForRegisReq);
    }

    @Override
    public SuccessCode addMovie(AddMovieReq addMovieReq) {
        Movie movie = movieManager.addMovie(addMovieReq);
        List<Genre> genre = movieManager.getGenre(movie.getId());
        movieManager.addStreamingPlatform(addMovieReq, movie);
        movieManager.addMovieElastic(movie, genre);
        return SuccessCode.CREATE_MOVIE_SUCCESS;
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
        Long linereviewCount = lineReviewManager.getLineReviewCount(movieId);
        return movieDtoMapper.toGetMovieDetailResp(movie, movieBehindVideos, genres, actors, directors, like, platforms, movie.getTotalRating(), linereviewCount);
    }

    @Transactional
    @Override
    public SuccessCode updateMovie(Long movieId, UpdateMovieReq updateMovieReq) {
        return movieManager.updateMovie(movieId, updateMovieReq);
    }

    @Override
    public List<GetRecommendMovieResp> getRecommends() {
        List<Long> movieIds = movieManager.getRecommendsFromAi(authManager.getUserId());
        List<Movie> movies = movieIds.stream()
                .map(movieId -> {
                    if (!movieManager.isExists(movieId)) {
                        AddMovieAuto addMovieAuto = movieManager.saveMovieAuto(movieId);
                        if (addMovieAuto == null) {
                            return null; // null 반환
                        }
                        Movie movie = movieManager.addMovieAuto(addMovieAuto);
                        movieManager.addMovieGenresAuto(addMovieAuto.genres(), movie);
                        movieManager.addActorsAuto(addMovieAuto.credits(), movie);
                        movieManager.addDirectorsAuto(addMovieAuto.credits(), movie);
                        List<Genre> genres = movieManager.getGenre(movieId);
                        movieManager.addMovieElastic(movie,genres);
                        return movie;
                    } else {
                        return movieManager.findByMovieId(movieId);
                    }
                })
                .filter(Objects::nonNull) // null 값을 필터링
                .collect(Collectors.toList());

        List<GetRecommendMovieProjection> simpleMovieProjections = movieManager.getRecommendsAi(movies);
        return movieDtoMapper.toGetRecommendMovies(simpleMovieProjections);
    }

    @Override
    public List<GetSimpleMovieResp> getTop10() {
        return movieManager.getTop10();
    }

    @Override
    public Slice<GetSimpleMovieResp> getMoviesByGenre(Long genreId, Long lastMovieId, LocalDateTime createdAt) {
        return movieManager.getMoviesByGenre(genreId, lastMovieId, createdAt);
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

    @Override
    public List<GetSearchMoviesResp> getSearchMovies(String keyword) {
        List<MovieDocument> movies = movieManager.getSearchMovies(keyword);
        return movieDtoMapper.toGetSearchMovies(movies);
    }

    private final MovieRepository movieRepository;
    @Override
    public List<GetSearchMoviesResp> getSearchMoviesMysql(String keyword) {
        // MySQL에서 검색된 영화들
        List<Tuple> tuples = movieRepository.findSearchMoviesMySQL(keyword);

        List<GetSearchMoviesMysqlResp> mysqlResps = tuples.stream()
                .map(tuple -> {
                    Long movieId = tuple.get("movieId", Long.class);
                    String movieTitle = tuple.get("movieTitle", String.class);
                    String moviePosterUrl = tuple.get("moviePosterUrl", String.class);
                    Date releaseDate = tuple.get("releaseDate", Date.class);
                    String originalLanguage = tuple.get("originalLanguage", String.class);

                    // DTO로 변환
                    return new GetSearchMoviesMysqlResp(movieId, movieTitle, moviePosterUrl, releaseDate, originalLanguage);
                })
                .collect(Collectors.toList());

        // Stream을 사용하여 resps 리스트 처리
        return mysqlResps.stream()
                .map(resp -> {
                    // 각 영화의 movieId
                    Long movieId = resp.movieId();

                    // movieManager를 통해 장르 리스트를 가져옴
                    List<Genre> genres = movieManager.getGenres();
                    List<GetGenres> getGenres = movieDtoMapper.toGetGenres(genres);

                    // GetSearchMoviesResp에 필요한 데이터 담기
                    return movieDtoMapper.toGetSearchMysqlMovies(resp, getGenres);
                })
                .collect(Collectors.toList()); // 최종 리스트로 변환하여 반환
    }

}
