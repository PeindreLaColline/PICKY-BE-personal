package com.ureca.picky_be.base.presentation.controller.movie;

import com.ureca.picky_be.base.business.movie.MovieService;
import com.ureca.picky_be.base.business.movie.dto.*;
import com.ureca.picky_be.global.success.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/movie")
public class MovieController {
    private final MovieService movieService;

    @Operation(summary = "영화 등록", description = "영화 등록 api - 담당자 김")
    @PostMapping
    public SuccessCode addMovie(@RequestBody AddMovieReq addMovieReq) {
        return movieService.addMovie(addMovieReq);
    }

    @Operation(summary = "영화 상세 정보", description = "영화 상세 정보 api - 담당자 김")
    @GetMapping("/{movieId}")
    public GetMovieDetailResp getMovieDetails(@PathVariable Long movieId) {
        return movieService.getMovieDetail(movieId);
    }

    @Operation(summary = "영화 정보 업데이트", description = "영화 정보 업데이트 api - 담당자 김")
    @PatchMapping("/{movieId}")
    public SuccessCode updateMovie(@PathVariable Long movieId, @RequestBody UpdateMovieReq updateMovieReq) {
        return movieService.updateMovie(movieId, updateMovieReq);
    }

    @Operation(summary = "영화 추천 리스트(30개)", description = "AI 개발 완료 전까지 임시로 랜덤으로 보냅니다. 프론트에서는 그냥 그대로 개발하시면 됩나다.")
    @GetMapping("/recommend")
    public List<GetSimpleMovieResp> getRecommendMovies() {
        return movieService.getRecommends();
    }

    @Operation(summary = "영화 top10", description = "평점 기준 top 10 보냅니다 (임시입니다 나중에 멋진 로직 짤 예정)")
    @GetMapping("/top10")
    public List<GetSimpleMovieResp> getTop10Movies() {
        return movieService.getTop10();
    }

    @Operation(summary = "영화 장르별 조회", description = "영화 장르별 조회")
    @GetMapping("/genre")
    public List<GetSimpleMovieResp> getMoviesByGenre(@RequestBody GetMovieByGenreReq getMovieByGenreReq) {
        return movieService.getMoviesByGenre(getMovieByGenreReq);
    }

    @Operation(summary = "영화 좋아요", description = "영화 좋아요 혹은 좋아요 취소. return true일 시 좋아요 눌린 상태, false일 시 좋아요 안 눌린 상태")
    @PostMapping("/{movieId}/like")
    public boolean movieLike(@PathVariable Long movieId){
        return movieService.movieLike(movieId);
    }
}