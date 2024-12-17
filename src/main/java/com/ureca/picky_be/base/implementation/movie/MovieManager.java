package com.ureca.picky_be.base.implementation.movie;

import com.ureca.picky_be.base.business.movie.dto.*;
import com.ureca.picky_be.base.business.user.dto.GetMoviesForRegisReq;
import com.ureca.picky_be.base.business.user.dto.GetMoviesForRegisResp;
import com.ureca.picky_be.base.persistence.movie.*;
import com.ureca.picky_be.base.persistence.movieworker.MovieWorkerRepository;
import com.ureca.picky_be.base.persistence.user.UserRepository;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.entity.config.IsDeleted;
import com.ureca.picky_be.jpa.entity.genre.Genre;
import com.ureca.picky_be.jpa.entity.movie.*;
import com.ureca.picky_be.jpa.entity.movieworker.MovieWorker;
import com.ureca.picky_be.jpa.entity.platform.Platform;
import com.ureca.picky_be.jpa.entity.platform.PlatformType;
import com.ureca.picky_be.jpa.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MovieManager {
    // -------------------------- 의존성 --------------------------
    private final MovieRepository movieRepository;
    private final MovieGenreRepository movieGenreRepository;
    private final MovieBehindVideoRepository movieBehindVideoRepository;
    private final MovieWorkerRepository movieWorkerRepository;
    private final FilmCrewRepository filmCrewRepository;
    private final GenreRepository genreRepository;
    private final MovieLikeRepository movieLikeRepository;
    private final UserRepository userRepository;
    private final PlatformRepository platformRepository;
    private final RecommendRepository recommendRepository;

    RestClient restClient = RestClient.create();

    @Value("${tmdb.token}")
    private String tmdbToken;

    @Value("${tmdb.url}")
    private String tmdbUrl;
    // -------------------------- 메서드 --------------------------
    // <editor-fold desc="영화 조회">
    /**
     * when: 회원가입시
     * what: 회원이 선택한 장르의 영화를 높은 평점순으로 반환
     */
    @Transactional(readOnly = true)
    public List<GetMoviesForRegisResp> getMoviesByGenres(GetMoviesForRegisReq getMoviesForRegisReq) {
        //TODO: 장르 없을 때 예외
        validateGenreIds(getMoviesForRegisReq.genreIds());
        Pageable pageable = PageRequest.of(0, 45);
        return movieRepository.findMovieByGenresOrderByTotalRating(getMoviesForRegisReq.genreIds(), pageable);
    }

    /**
     * when: 사용자가 AI 영화 추천 목록 조회시 (step1)
     * what: 해당 사용자에게 매칭되어있는 MovieId 리스트 반환
     */
    public List<Long> getRecommendsFromAi(Long userId){
        return recommendRepository.findAllMovieIdsByUserId(userId);
    }

    /**
     * when: 사용자가 AI 영화 추천 목록 조회시 (step2)
     * what: AI로 추천된 영화를 DtoProjection으로 반환
     */
    public List<GetRecommendMovieProjection> getRecommendsAi(List<Movie> movies){
        List<Long> movieIds = movies.stream()
                .map(Movie::getId) // ID 추출
                .collect(Collectors.toList());
        return movieRepository.findMoviesWithGenresAndPlatforms(movieIds);
    }

    /**
     * when: 사용자가 AI 영화 추천 목록 조회시 (etc) - 아직 추천 목록이 생성되지 않았을 때
     * what: 높은 좋아요 순으로 영화 반환
     */
    public List<GetSimpleMovieResp> getRecommends(){
        Pageable pageable = PageRequest.of(0, 30);
        return movieRepository.findTop30MoviesWithLikes(pageable);
    }

    /**
     * when: 영화 메인페이지
     * what: top 10 영화 리스트 반환
     */
    public List<GetSimpleMovieResp> getTop10(){
        Pageable pageable = PageRequest.of(0, 10);
        return movieRepository.findTop10MoviesWithLikes(pageable);
    }

    /**
     * when: 장르별 영화 조회
     * what: 12개씩 장르별로 높은 좋아요 순으로 조회
     */
    public Slice<GetSimpleMovieResp> getMoviesByGenre(Long genreId, Long lastMovieId, Integer lastLikeCount){
/*        // 영화 존재 여부 확인
        if (lastMovieId != null && !movieRepository.existsById(lastMovieId)) {
            throw new CustomException(ErrorCode.MOVIE_NOT_FOUND);
        }*/

        //validateCursor(genreId, lastMovieId);
        return movieRepository.findMoviesByGenreIdWithLikesUsingCursor(genreId, lastLikeCount, lastMovieId, PageRequest.ofSize(12));
    }
    // </editor-fold>
    // <editor-fold desc="영화 조회에 필요한 메서드">
    public Movie getMovie(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));
    }

    public List<MovieBehindVideo> getMovieBehindVideos(Long movieId) {
        return movieBehindVideoRepository.findAllByMovieId(movieId);
    }

    public List<Genre> getGenre(Long movieId) {
        List<Long> genreIds = movieGenreRepository.getGenreIdsByMovieId(movieId);
        List<Genre> genres = genreRepository.findAllByIdIn(genreIds);
        if (genres.isEmpty()) {
            throw new CustomException(ErrorCode.GENRE_NOT_FOUND);
        }
        return genres;
    }

    public List<FilmCrew> getActors(Movie movie) {
        List<FilmCrew> actors = filmCrewRepository.findByMovieAndFilmCrewPosition(movie, FilmCrewPosition.ACTOR);
        if (actors.isEmpty()) {
            throw new CustomException(ErrorCode.ACTOR_NOT_FOUND);
        }
        return actors;
    }

    public List<FilmCrew> getDirectors(Movie movie) {
        List<FilmCrew> directors = filmCrewRepository.findByMovieAndFilmCrewPosition(movie, FilmCrewPosition.DIRECTOR);
        if(directors.isEmpty()) {
            throw new CustomException(ErrorCode.DIRECTOR_NOT_FOUND);
        }
        return directors;
    }

    public boolean getMovieLike(Long movieId, Long userId){
        return movieLikeRepository.existsByMovieIdAndUserId(movieId, userId);
    }

    public List<Platform> getStreamingPlatform(Movie movie) {
        return platformRepository.findAllByMovie(movie);
    }
    // </editor-fold>
    // <editor-fold desc="영화 추가">
    /**
     * when: 관리자가 영화 등록시
     * what: 프론트에서 받아온 정보들로 영화 등록
     */
    public Movie addMovie(AddMovieReq addMovieReq) {
        if(movieRepository.existsById(addMovieReq.movieInfo().id())){
            throw new CustomException(ErrorCode.MOVIE_EXISTS);
        }

        Movie movie = addMovieInfo(addMovieReq);
        List<MovieBehindVideo> movieBehindVideos = addMovieBehindVideos(addMovieReq.movieBehindVideos(), movie);
        List<MovieGenre> movieGenres = addMovieGenres(addMovieReq.movieInfo().genres(), movie);
        List<FilmCrew> actors = addActors(addMovieReq.movieInfo().credits(), movie);
        List<FilmCrew> directors = addDirectors(addMovieReq.movieInfo().credits(), movie);
        return movie;
    }

    /**
     * when: 사용자가 AI추천 영화 목록 조회시
     * what: 우리 DB에 없는 영화를 외부 API를 통해 정보 불러옴
     */
    public AddMovieAuto saveMovieAuto(Long movieId){
        try{
            return restClient
                    .get()
                    .uri(buildTmdbUrl(movieId))
                    .headers(headers -> headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + tmdbToken))
                    .retrieve()
                    .body(AddMovieAuto.class);
        } catch (Exception e) {
            return null;
            //throw new CustomException(ErrorCode.VALIDATION_ERROR);
        }
    }

    /**
     * when: 사용자가 AI추천 영화 목록 조회시
     * what: 외부 API를 통해 가져온 정보를 우리 DB에 래핑
     */
    public Movie addMovieAuto(AddMovieAuto addMovieAuto) {
        Movie movie = Movie.builder()
                .id(addMovieAuto.id())
                .title(addMovieAuto.title())
                .releaseDate(addMovieAuto.releaseDate())
                .posterUrl(addMovieAuto.posterUrl())
                .backdropUrl(addMovieAuto.backdropUrl())
                .totalRating(2.5)
                .plot(addMovieAuto.plot())
                .runningTime(addMovieAuto.runtime())
                .isDeleted(IsDeleted.FALSE)
                .originalLanguage(addMovieAuto.originalLanguage())
                .popularity(addMovieAuto.popularity())
                .build();
        return movieRepository.save(movie);
    }
    // </editor-fold>
    // <editor-fold desc="영화 추가에 필요한 메서드">
    private Movie addMovieInfo(AddMovieReq addMovieReq) {
        Movie movie = Movie.builder()
                .id(addMovieReq.movieInfo().id())
                .title(addMovieReq.movieInfo().title())
                .releaseDate(addMovieReq.movieInfo().releaseDate())
                .posterUrl(addMovieReq.movieInfo().posterUrl())
                .backdropUrl(addMovieReq.movieInfo().backdropUrl())
                .totalRating(2.5)
                .plot(addMovieReq.movieInfo().plot())
                .runningTime(addMovieReq.movieInfo().runtime())
                .trailerUrl(addMovieReq.trailer())
                .ostUrl(addMovieReq.ost())
                .isDeleted(IsDeleted.FALSE)
                .originalLanguage(addMovieReq.movieInfo().originalLanguage())
                .popularity(addMovieReq.movieInfo().popularity())
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
        System.out.println("addMovieGenres");
        List<MovieGenre> movieGenres = genres.stream()
                .map(genre -> MovieGenre.builder()
                        .movie(movie)
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
                            .movieWorker(movieWorker)
                            .movie(movie)
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
                            .movieWorker(movieWorker)
                            .movie(movie)
                            .role("감독감독~!")
                            .filmCrewPosition(FilmCrewPosition.DIRECTOR)
                            .build();
                })
                .toList();
        return filmCrewRepository.saveAll(filmCrews);
    }

    public SuccessCode addStreamingPlatform(AddMovieReq addMovieReq, Movie movie) {
        if(addMovieReq.streamingPlatform().coupang()){
            Platform platform = Platform.builder()
                    .platformType(PlatformType.COUPANG)
                    .movie(movie)
                    .build();
            platformRepository.save(platform);
        }
        if(addMovieReq.streamingPlatform().disney()){
            Platform platform = Platform.builder()
                    .platformType(PlatformType.DISNEY)
                    .movie(movie)
                    .build();
            platformRepository.save(platform);
        }
        if(addMovieReq.streamingPlatform().netflix()){
            Platform platform = Platform.builder()
                    .platformType(PlatformType.NETFLIX)
                    .movie(movie)
                    .build();
            platformRepository.save(platform);
        }
        if(addMovieReq.streamingPlatform().tving()){
            Platform platform = Platform.builder()
                    .platformType(PlatformType.TVING)
                    .movie(movie)
                    .build();
            platformRepository.save(platform);
        }
        if(addMovieReq.streamingPlatform().wavve()){
            Platform platform = Platform.builder()
                    .platformType(PlatformType.WAVVE)
                    .movie(movie)
                    .build();
            platformRepository.save(platform);
        }
        if(addMovieReq.streamingPlatform().watcha()){
            Platform platform = Platform.builder()
                    .platformType(PlatformType.WATCHA)
                    .movie(movie)
                    .build();
            platformRepository.save(platform);
        }
        return SuccessCode.CREATE_MOVIE_SUCCESS;
    }
    // </editor-fold>
    // <editor-fold desc="영화 자동 추가에 필요한 메서드">
    public List<MovieGenre> addMovieGenresAuto(List<AddMovieReq.MovieInfo.GenreInfo> genres, Movie movie) {
        List<MovieGenre> movieGenres = genres.stream()
                .map(genre -> MovieGenre.builder()
                        .movie(movie)
                        .genreId(genre.id())
                        .build())
                .toList();
        return movieGenreRepository.saveAll(movieGenres);
    }

    public List<FilmCrew> addActorsAuto(AddMovieReq.MovieInfo.Credits credits, Movie movie) {
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
                            .movieWorker(movieWorker)
                            .movie(movie)
                            .role(cast.role())
                            .filmCrewPosition(FilmCrewPosition.ACTOR)
                            .build();
                })
                .toList();
        return filmCrewRepository.saveAll(filmCrews);
    }

    public List<FilmCrew> addDirectorsAuto(AddMovieReq.MovieInfo.Credits credits, Movie movie) {
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
                            .movieWorker(movieWorker)
                            .movie(movie)
                            .role("감독감독~!")
                            .filmCrewPosition(FilmCrewPosition.DIRECTOR)
                            .build();
                })
                .toList();
        return filmCrewRepository.saveAll(filmCrews);
    }
    // </editor-fold>
    // <editor-fold desc="영화 수정">
    public SuccessCode updateMovie (Long movieId, UpdateMovieReq updateMovieReq){
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));
        if(updateMovieReq.movieInfo() != null){
            updateMovieInfo(movie, updateMovieReq);
            if(updateMovieReq.movieInfo().genres() != null){
                updateMovieGenre(movie, updateMovieReq.movieInfo().genres());
            }
            if(updateMovieReq.movieInfo().credits() != null){
                if(updateMovieReq.movieInfo().credits().cast() != null){
                    updateActors(movie, updateMovieReq.movieInfo().credits().cast());
                }

                if(updateMovieReq.movieInfo().credits().crew() != null){
                    updateDirectors(movie, updateMovieReq.movieInfo().credits().getDirectingCrew());
                }
            }
        }
        if(updateMovieReq.streamingPlatform() != null){
            updateStreamingPlatform(updateMovieReq, movie);
        }
        if(updateMovieReq.trailer() != null){
            updateTrailer(updateMovieReq.trailer(), movie);
        }
        if(updateMovieReq.ost() != null){
            updateOst(updateMovieReq.ost(), movie);
        }
        if(updateMovieReq.movieBehindVideos() != null){
            updateBehindVideo(updateMovieReq.movieBehindVideos(), movie);
        }
        return SuccessCode.UPDATE_MOVIE_SUCCESS;
    }
    // </editor-fold>
    // <editor-fold desc="영화 수정에 필요한 메서드">
    private void updateBehindVideo(List<String> behindVideos, Movie movie) {
        movieBehindVideoRepository.deleteAllByMovieId(movie.getId());
        List<MovieBehindVideo> movieBehindVideos = behindVideos.stream()
                .map(url -> MovieBehindVideo.builder()
                        .url(url)
                        .movie(movie)
                        .build())
                .toList();
        movieBehindVideoRepository.saveAll(movieBehindVideos);
    }

    private void updateOst(String ostUrl, Movie movie){
        movie.updateOst(ostUrl);
    }

    private void updateTrailer(String trailerUrl, Movie movie){
        movie.updateTrailer(trailerUrl);
    }

    private void updateStreamingPlatform(UpdateMovieReq updateMovieReq, Movie movie) {
        platformRepository.deleteAllByMovie(movie);
        if(updateMovieReq.streamingPlatform().coupang()){
            Platform platform = Platform.builder()
                    .platformType(PlatformType.COUPANG)
                    .movie(movie)
                    .build();
            platformRepository.save(platform);
        }
        if(updateMovieReq.streamingPlatform().disney()){
            Platform platform = Platform.builder()
                    .platformType(PlatformType.DISNEY)
                    .movie(movie)
                    .build();
            platformRepository.save(platform);
        }
        if(updateMovieReq.streamingPlatform().netflix()){
            Platform platform = Platform.builder()
                    .platformType(PlatformType.NETFLIX)
                    .movie(movie)
                    .build();
            platformRepository.save(platform);
        }
        if(updateMovieReq.streamingPlatform().tving()){
            Platform platform = Platform.builder()
                    .platformType(PlatformType.TVING)
                    .movie(movie)
                    .build();
            platformRepository.save(platform);
        }
        if(updateMovieReq.streamingPlatform().wavve()){
            Platform platform = Platform.builder()
                    .platformType(PlatformType.WAVVE)
                    .movie(movie)
                    .build();
            platformRepository.save(platform);
        }
        if(updateMovieReq.streamingPlatform().watcha()){
            Platform platform = Platform.builder()
                    .platformType(PlatformType.WATCHA)
                    .movie(movie)
                    .build();
            platformRepository.save(platform);
        }
    }

    private Movie updateMovieInfo(Movie movie, UpdateMovieReq updateMovieReq){
        movie = movie.updateMovie(updateMovieReq);
        return movieRepository.save(movie);
    }

    private List<MovieGenre> updateMovieGenre(Movie movie, List<UpdateMovieReq.MovieInfo.GenreInfo> genres){
        movieGenreRepository.deleteMovieGenreByMovieId(movie.getId());
        List<MovieGenre> movieGenres = genres.stream()
                .map(genre -> MovieGenre.builder()
                        .movie(movie)
                        .genreId(genre.id())
                        .build())
                .toList();
        return movieGenreRepository.saveAll(movieGenres);
    }

    private List<FilmCrew> updateActors(Movie movie, List<UpdateMovieReq.MovieInfo.Credits.Cast> casts){
        filmCrewRepository.deleteActorsByMovieId(movie.getId());
        List<FilmCrew> filmCrews = casts.stream()
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
                            .movieWorker(movieWorker)
                            .movie(movie)
                            .role(cast.role())
                            .filmCrewPosition(FilmCrewPosition.ACTOR)
                            .build();
                })
                .toList();
        return filmCrewRepository.saveAll(filmCrews);
    }

    private List<FilmCrew> updateDirectors(Movie movie, List<UpdateMovieReq.MovieInfo.Credits.Crew> crews){
        filmCrewRepository.deleteDirectorsByMovieId(movie.getId());
        List<FilmCrew> filmCrews = crews.stream()
                .limit(10)
                .map(crew -> {
                    MovieWorker movieWorker = movieWorkerRepository.findById(crew.id())
                            .orElseGet(() -> movieWorkerRepository.save(
                                    MovieWorker.builder()
                                            .id(crew.id())
                                            .name(crew.name())
                                            .profileUrl(crew.profileUrl())
                                            .build()
                            ));

                    return FilmCrew.builder()
                            .movieWorker(movieWorker)
                            .movie(movie)
                            .role("감독감독~!")
                            .filmCrewPosition(FilmCrewPosition.DIRECTOR)
                            .build();
                })
                .toList();
        return filmCrewRepository.saveAll(filmCrews);
    }
    // </editor-fold>
    // <editor-fold desc="유효성 검사">
    /**
     * when: 회원가입시에 장르별 영화 조회시
     * what: 입력받은 영화 장르의 유효성 검사
     */
    private void validateGenreIds(List<Long> genreIds) {
        List<Long> invalidGenreIds = genreIds.stream()
                .filter(id -> !genreRepository.existsById(id))
                .toList();
        if (!invalidGenreIds.isEmpty()) {
            throw new CustomException(ErrorCode.GENRE_NOT_FOUND);
        }
    }

    /**
     * when: whenever
     * what: movieId를 통해 해당 영화가 우리 DB에 존재 여부 확인
     */
    public boolean isExists(Long movieId){
        return movieRepository.existsById(movieId);
    }

    /**
     * when: whenever
     * what: movieId를 통해 Movie entity 반환
     */
    public Movie findByMovieId(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));
    }

    private void validateCursor(Long genreId, Long movieId) {
        // 첫 요청일 경우
        if (movieId == null) return;

        // genreId 유효성 검사
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new CustomException(ErrorCode.GENRE_NOT_FOUND));
    }

    private void lastMovieLikeIdValidation(Long lastMovieLikeId) {
        if(lastMovieLikeId == null) return;
        if(lastMovieLikeId <= 0) {
            throw new CustomException(ErrorCode.MOVIE_LIKE_INVALID_CURSOR);
        }
    }
    // </editor-fold>
    // <editor-fold desc="기타">
    /**
     * when: 사용자가 AI추천 영화 목록 조회시
     * what: 외부 API 호출을 위한 url build
     */
    private String buildTmdbUrl(Long movieId){
        String sd = UriComponentsBuilder
                .fromHttpUrl(tmdbUrl + movieId)
                .queryParam("append_to_response", "credits")
                .queryParam("language", "ko-KR")
                .build()
                .toUriString();
        System.out.println(sd);
        return sd;
    }

    // </editor-fold>

    @Transactional
    public boolean movieLike(Long movieId, Long userId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new CustomException(ErrorCode.MOVIE_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return movieLikeRepository.findByMovieIdAndUserId(movieId, userId)
                .map(movieLike -> {
                    movieLikeRepository.delete(movieLike);
                    return false;
                })
                .orElseGet(() -> {
                    createNewMovieLike(movie, user);
                    return true;
                });
    }

    private void createNewMovieLike(Movie movie, User user){
        try{
            MovieLike newMovieLike = MovieLike.builder()
                    .movie(movie)
                    .user(user)
                    .build();
            movieLikeRepository.save(newMovieLike);
        } catch (Exception e){
            throw new CustomException(ErrorCode.MOVIE_LIKE_FAILED);
        }
    }

    public List<Genre> getGenres(){
        return genreRepository.findAll();
    }

    public List<GetSimpleMovieProjection> getMoviesOrderByCreatedAt(Long lastMovieId, LocalDateTime createdAt, int size){
        return movieRepository.findMoviesOrderByCreatedAtUsingCursor(lastMovieId, createdAt, PageRequest.ofSize(size));
    }

    public Slice<GetUserLikeMovieResp> findLikeMoviesByNickname(Long userId, GetUserLikeMovieReq req, PageRequest pageRequest) {
        Long lastMovieLikeId = req.lastMovieLikeId();
        lastMovieLikeIdValidation(lastMovieLikeId);
        return movieLikeRepository.findByUserId(userId, lastMovieLikeId, pageRequest);
    }
}
