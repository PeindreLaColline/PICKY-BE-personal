package com.ureca.picky_be.base.implementation.mapper;

import com.ureca.picky_be.base.business.movie.dto.*;
import com.ureca.picky_be.base.business.user.dto.GetMoviesForRegisResp;
import com.ureca.picky_be.elasticsearch.document.movie.MovieDocument;
import com.ureca.picky_be.jpa.entity.genre.Genre;
import com.ureca.picky_be.jpa.entity.movie.FilmCrew;
import com.ureca.picky_be.jpa.entity.movie.Movie;
import com.ureca.picky_be.jpa.entity.movie.MovieBehindVideo;
import com.ureca.picky_be.jpa.entity.platform.Platform;
import com.ureca.picky_be.jpa.entity.platform.PlatformType;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MovieDtoMapper {

    public GetMovieDetailResp toGetMovieDetailResp(
            Movie movie,
            List<MovieBehindVideo> movieBehindVideos,
            List<Genre> genres,
            List<FilmCrew> actors,
            List<FilmCrew> directors,
            boolean like,
            List<Platform> platforms,
            double rating,
            Long linereviewCount
    ) {
        List<GetMovieDetailResp.MovieInfo.GenreInfo> genreInfoList = genres.stream()
                .map(genre -> new GetMovieDetailResp.MovieInfo.GenreInfo(genre.getId()))
                .toList();

        List<GetMovieDetailResp.MovieInfo.Credits.Cast> castList = actors.stream()
                .map(actor -> new GetMovieDetailResp.MovieInfo.Credits.Cast(
                        actor.getMovieWorker().getId(),
                        actor.getRole(),
                        actor.getMovieWorker().getName(),
                        actor.getMovieWorker().getProfileUrl()
                ))
                .toList();

        List<GetMovieDetailResp.MovieInfo.Credits.Crew> crewList = directors.stream()
                .map(director -> new GetMovieDetailResp.MovieInfo.Credits.Crew(
                        director.getMovieWorker().getId(),
                        "Director",
                        director.getMovieWorker().getName(),
                        director.getMovieWorker().getProfileUrl()
                ))
                .toList();

        List<String> movieBehindVideoUrls = movieBehindVideos.stream()
                .map(MovieBehindVideo::getUrl)
                .toList();

        Set<PlatformType> platformTypes = platforms.stream()
                .map(Platform::getPlatformType)
                .collect(Collectors.toSet());

        return new GetMovieDetailResp(
                new GetMovieDetailResp.MovieInfo(
                        movie.getId(),
                        movie.getTitle(),
                        movie.getReleaseDate(),
                        movie.getPosterUrl(),
                        movie.getBackdropUrl(),
                        movie.getPlot(),
                        movie.getRunningTime(),
                        genreInfoList,
                        new GetMovieDetailResp.MovieInfo.Credits(castList, crewList)
                ),
                Optional.ofNullable(movie.getTrailerUrl()).orElse("Trailer not found"),
                Optional.ofNullable(movie.getOstUrl()).orElse("OST not found"),
                movieBehindVideoUrls,
                like,
                rating,
                new GetMovieDetailResp.StreamingPlatform(
                        platformTypes.contains(PlatformType.NETFLIX),
                        platformTypes.contains(PlatformType.DISNEY),
                        platformTypes.contains(PlatformType.WATCHA),
                        platformTypes.contains(PlatformType.WAVVE),
                        platformTypes.contains(PlatformType.TVING),
                        platformTypes.contains(PlatformType.COUPANG)
                ),
                linereviewCount
        );
    }

    public List<GetGenres> toGetGenres(List<Genre> genres) {
        return genres.stream()
                .map(genre -> new GetGenres(genre.getId(), genre.getName()))
                .toList();
    }

    public Slice<GetSimpleMovieResp> toGetSimpleMovies(List<GetSimpleMovieProjection> projections) {
        List<GetSimpleMovieResp> simpleMovieRespList = projections.stream()
                .map(projection -> new GetSimpleMovieResp(
                        projection.getMovieId(),
                        projection.getTitle(),
                        projection.getLikes(),
                        projection.getLineReviews(),
                        projection.getCreatedAt(),
                        projection.getTotalRating(),
                        projection.getPosterUrl(),
                        projection.getBackdropUrl()
                ))
                .collect(Collectors.toList());

        return new SliceImpl<>(simpleMovieRespList);
    }

    public List<GetRecommendMovieResp> toGetRecommendMovies(List<GetRecommendMovieProjection> simpleMovieProjections) {
        Map<Long, GetRecommendMovieResp> movieMap = new LinkedHashMap<>();

        for (GetRecommendMovieProjection projection : simpleMovieProjections) {
            movieMap.computeIfAbsent(projection.getMovieId(), movieId -> new GetRecommendMovieResp(
                    projection.getMovieId(),
                    projection.getTitle(),
                    projection.getTotalRating(),
                    projection.getPosterUrl(),
                    new ArrayList<>(),
                    new ArrayList<>()
            ));

            List<GetGenres> genres = movieMap.get(projection.getMovieId()).genres();
            if (projection.getGenreId() != null && projection.getGenreName() != null) {
                boolean isDuplicateGenre = genres.stream()
                        .anyMatch(g -> g.genreId().equals(projection.getGenreId()));
                if (!isDuplicateGenre) {
                    genres.add(new GetGenres(projection.getGenreId(), projection.getGenreName()));
                }
            }

            // 플랫폼 중복 제거 후 추가
            List<GetPlatform> platforms = movieMap.get(projection.getMovieId()).platforms();
            if (projection.getPlatformId() != null && projection.getPlatformName() != null) {
                boolean isDuplicatePlatform = platforms.stream()
                        .anyMatch(p -> p.platformId().equals(projection.getPlatformId()));
                if (!isDuplicatePlatform) {
                    platforms.add(new GetPlatform(
                            projection.getPlatformId(),
                            projection.getPlatformName(),
                            projection.getPlatformUrl() // null일 경우에도 추가
                    ));
                }
            }
        }

        return new ArrayList<>(movieMap.values());
    }

    public List<GetSearchMoviesResp> toGetSearchMovies(List<MovieDocument> movies) {
        return movies.stream()
                .map(movie -> new GetSearchMoviesResp(
                        movie.getId(),
                        movie.getTitle(),
                        movie.getPosterUrl(),
                        movie.getReleaseDate(),
                        movie.getGenre(), // 그대로 전달
                        movie.getOriginalLanguage()
                )).toList();
    }

    public GetSearchMoviesResp toGetSearchMysqlMovies(GetSearchMoviesMysqlResp resp, List<GetGenres> genres){
        return new GetSearchMoviesResp(
                resp.movieId(),
                resp.movieTitle(),
                resp.moviePosterUrl(),
                resp.releaseDate(),
                genres,
                resp.originalLanguage()
        );
    }
}