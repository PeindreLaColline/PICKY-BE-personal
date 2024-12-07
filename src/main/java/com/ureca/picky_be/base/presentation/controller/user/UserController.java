package com.ureca.picky_be.base.presentation.controller.user;

import com.ureca.picky_be.base.business.movie.MovieUseCase;
import com.ureca.picky_be.base.business.movie.dto.GetGenres;
import com.ureca.picky_be.base.business.user.UserUseCase;
import com.ureca.picky_be.base.business.user.dto.*;
import com.ureca.picky_be.global.success.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserUseCase userUseCase;
    private final MovieUseCase movieUseCase;

    @Operation(summary = "회원가입할 때 개인정보 기입, 마이페이지에서 개인정보 수정", description = "무조건 모든 필드 다 채워서 주세요. movieId, genreId 말고는 null 불가")
    @PatchMapping
    public SuccessCode updateUserInfo(@RequestBody UpdateUserReq req) {
        return userUseCase.updateUserInfo(req);
    }

    @Operation(summary = "유저 개인정보 조회", description = "유저 개인정보 조회")
    @GetMapping
    public GetUserResp getUserInfo(){
        return userUseCase.getUserInfo();
    }

    @Operation(summary = "회원가입할 때 선택할 영화 리스트 전송", description = "회원가입할 때 선택할 영화 리스트 전송")
    @PostMapping("/movies-by-genres")
    public List<GetMoviesForRegisResp> getMovieListByGenre(@RequestBody GetMoviesForRegisReq req){
        return movieUseCase.getMoviesByGenre(req);
    }

    @Operation(summary ="장르 전체 리스트 반환", description = "회원가입 때 쓸 일 있으시면 쓰세요.")
    @GetMapping("/genres")
    public List<GetGenres> getGenres() {
        return movieUseCase.getGenres();
    }

    @Operation(summary = "회원가입 시 닉네임 중복 체크", description = "회원가입시 닉네임 중복 체크")
    @GetMapping("/nickname-validation/{nickname}")
    public GetNicknameValidationResp isValid(@PathVariable String nickname){
        return userUseCase.getNicknameValidation(nickname);
    }

}