package com.ureca.picky_be.base.presentation.controller.movie;

import com.ureca.picky_be.base.business.movie.MovieService;
import com.ureca.picky_be.base.business.movie.dto.AddMovieReq;
import com.ureca.picky_be.base.business.movie.dto.GetMovieDetailResp;
import com.ureca.picky_be.base.business.movie.dto.UpdateMovieReq;
import com.ureca.picky_be.global.success.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "영화 추천 리스트", description = "AI 개발 완료 전까지 임시로 랜덤으로 보냅니다. 프론트에서는 그냥 그대로 개발하시면 됩나다.")
    @GetMapping("/recommend")
    public SuccessCode getRecommendMovies() {
        //30개
        return SuccessCode.CREATE_MOVIE_SUCCESS;
    }
}
