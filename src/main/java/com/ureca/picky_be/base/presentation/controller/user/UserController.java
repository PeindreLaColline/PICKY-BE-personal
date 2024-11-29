package com.ureca.picky_be.base.presentation.controller.user;

import com.ureca.picky_be.base.business.movie.MovieUseCase;
import com.ureca.picky_be.base.business.movie.dto.MoviePreferenceResp;
import com.ureca.picky_be.base.business.user.UserUseCase;
import com.ureca.picky_be.base.business.user.dto.GetUserResp;
import com.ureca.picky_be.base.business.user.dto.UpdateUserReq;
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

    @Operation(summary = "첫 화면에서 유저 개인정보 기입, 및 마이페이지에서 개인정보 수정", description = "수정 안 하는 필드는 그냥 비워서 보내주세요")
    @PatchMapping
    public SuccessCode updateUserInfo(@RequestBody UpdateUserReq req) {
        return userUseCase.updateUserInfo(req);
    }

    @Operation(summary = "유저 개인정보 받기", description = "유저 개인정보 받기")
    @GetMapping
    public GetUserResp getUserInfo(){
        return userUseCase.getUserInfo();
    }

    @Operation(summary = "회원가입할 때 선택할 영화 리스트 전송", description = "회원가입할 때 선택할 영화 리스트 전송")
    @GetMapping("/movie-preference")
    public List<MoviePreferenceResp> getMovieListForPreference(){
        return movieUseCase.getMovieListForPreference();
    }
}
