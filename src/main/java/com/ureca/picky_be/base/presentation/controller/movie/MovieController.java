package com.ureca.picky_be.base.presentation.controller.movie;

import com.ureca.picky_be.base.business.movie.MovieService;
import com.ureca.picky_be.base.business.movie.dto.AddMovieReq;
import com.ureca.picky_be.base.business.movie.dto.GetMovieDetailResp;
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
        //TODO: token을 통해 관리자인지 확인해야함
        return movieService.addMovie(addMovieReq);
    }

    @Operation(summary = "영화 상세 정보", description = "영화 상세 정보 api - 담당자 김")
    @GetMapping("/{movieId}")
    public GetMovieDetailResp getMovieDetails(@PathVariable Long movieId) {
        return movieService.getMovieDetail(movieId);
    }
}
