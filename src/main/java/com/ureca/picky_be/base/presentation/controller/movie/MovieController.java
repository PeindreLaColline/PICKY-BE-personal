package com.ureca.picky_be.base.presentation.controller.movie;

import com.ureca.picky_be.base.business.lineReview.dto.GetUserLineReviewResp;
import com.ureca.picky_be.base.business.movie.MovieUseCase;
import com.ureca.picky_be.base.business.movie.dto.*;
import com.ureca.picky_be.base.business.user.dto.UserLineReviewsReq;
import com.ureca.picky_be.base.implementation.mapper.MovieDtoMapper;
import com.ureca.picky_be.global.success.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "영화 추천 리스트(30개)", description = "AI 개발 완료 전까지 임시로 랜덤으로 보냅니다. 프론트에서는 그냥 그대로 개발하시면 됩나다.")
    @GetMapping("/recommend")
    public List<GetSimpleMovieResp> getRecommendMovies() {
        return movieUseCase.getRecommends();
    }

    @Operation(summary = "영화 top10", description = "평점 기준 top 10 보냅니다 (임시입니다 나중에 멋진 로직 짤 예정)")
    @GetMapping("/top10")
    public List<GetSimpleMovieResp> getTop10Movies() {
        return movieUseCase.getTop10();
    }

    @Operation(summary = "영화 장르별 조회", description = "영화 장르별 조회")
    @GetMapping("/genre")
    public List<GetSimpleMovieResp> getMoviesByGenre(@RequestParam Long genreId,
                                                     @RequestParam(required = false) Long lastMovieId,
                                                     @RequestParam(required = false) Integer lastLikeCount) {
        return movieUseCase.getMoviesByGenre(genreId, lastMovieId, lastLikeCount);
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
            @RequestParam(defaultValue = "0", required = false) Long lastMovieLikeId) {

        GetUserLikeMovieReq req = new GetUserLikeMovieReq(nickname, lastMovieLikeId);

        return movieUseCase.getUserLikeMoviesByNickname(PageRequest.ofSize(size), req);
    }

}