package com.ureca.picky_be.base.presentation.controller.movie;

import com.ureca.picky_be.base.business.movie.MovieUseCase;
import com.ureca.picky_be.base.business.movie.dto.*;
import com.ureca.picky_be.global.success.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/movie")
public class MovieController {
    private final MovieUseCase movieUseCase;

    @Operation(summary = "영화 등록", description = "영화 등록 api - 담당자 김")
    @PostMapping
    public SuccessCode addMovie(@RequestBody AddMovieReq addMovieReq) {
        return movieUseCase.addMovie(addMovieReq);
    }

    @Operation(summary = "영화 상세 정보", description = "영화 상세 정보 api - 담당자 김")
    @GetMapping("/{movieId}")
    public GetMovieDetailResp getMovieDetails(@PathVariable Long movieId) {
        return movieUseCase.getMovieDetail(movieId);
    }

    @Operation(summary = "영화 정보 업데이트", description = "영화 정보 업데이트 api - 담당자 김")
    @PatchMapping("/{movieId}")
    public SuccessCode updateMovie(@PathVariable Long movieId, @RequestBody UpdateMovieReq updateMovieReq) {
        return movieUseCase.updateMovie(movieId, updateMovieReq);
    }

    @Operation(summary = "영화 추천 리스트(30개)", description = "AI가 추천하는 영화목록! 영화 자동 추가 가능")
    @GetMapping("/recommend")
    public List<GetRecommendMovieResp> getRecommendMovies() {
        return movieUseCase.getRecommends();
    }

    @Operation(summary = "영화 top10", description = "평점 기준 top 10 보냅니다 (임시입니다 나중에 멋진 로직 짤 예정)")
    @GetMapping("/top10")
    public List<GetSimpleMovieResp> getTop10Movies() {
        return movieUseCase.getTop10();
    }

    @Operation(summary = "영화 장르별 조회", description = "영화 장르별 조회")
    @GetMapping("/genre")
    public Slice<GetSimpleMovieResp> getMoviesByGenre(@RequestParam Long genreId,
                                                      @RequestParam(required = false) Long lastMovieId,
                                                      @RequestParam(required = false) LocalDateTime createdAt) {
        return movieUseCase.getMoviesByGenre(genreId, lastMovieId, createdAt);
    }

    @Operation(summary = "영화 좋아요", description = "영화 좋아요 혹은 좋아요 취소. return true일 시 좋아요 눌린 상태, false일 시 좋아요 안 눌린 상태")
    @PostMapping("/{movieId}/like")
    public boolean movieLike(@PathVariable Long movieId){
        return movieUseCase.movieLike(movieId);
    }


    @GetMapping("user/{nickname}")
    @Operation(summary = "닉네임으로 해당 사용자가 좋아요 누른 영화들 조회", description = "마이페이지에서 사용자 닉네임으로 해당 사용자가 좋아요 누른 영화들을 확인하는 API입니다.")
    public Slice<GetUserLikeMovieResp> getUserLikeMovies(
            @PathVariable String nickname,
            @Parameter(description = "0 < size <= 10") @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long lastMovieLikeId) {

        GetUserLikeMovieReq req = new GetUserLikeMovieReq(nickname, lastMovieLikeId);

        return movieUseCase.getUserLikeMoviesByNickname(PageRequest.ofSize(size), req);
    }

    @GetMapping("/search")
    @Operation(summary = "영화 검색 - Elastic Search")
    public List<GetSearchMoviesResp> getSearchMovies (@RequestParam("keyword") String keyword) {
        return movieUseCase.getSearchMovies(keyword);
    }

    @GetMapping("/search-mysql")
    @Operation(summary = "영화 검색 - MySQL")
    public List<GetSearchMoviesResp> getSearchMoviesMysql (@RequestParam("keyword") String keyword) {
        return movieUseCase.getSearchMoviesMysql(keyword);
    }

}