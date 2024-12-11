package com.ureca.picky_be.base.presentation.controller.user;

import com.ureca.picky_be.base.business.lineReview.dto.LineReviewQueryRequest;
import com.ureca.picky_be.base.business.lineReview.dto.ReadLineReviewResp;
import com.ureca.picky_be.base.business.movie.MovieUseCase;
import com.ureca.picky_be.base.business.movie.dto.GetGenres;
import com.ureca.picky_be.base.business.user.UserUseCase;
import com.ureca.picky_be.base.business.user.dto.*;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.lineReview.SortType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserUseCase userUseCase;
    private final MovieUseCase movieUseCase;

    @Operation(summary = "회원가입할 때 개인정보 기입", description = "무조건 모든 필드 다 채워서 주세요 (프로필 설정은 다른 api)")
    @PatchMapping
    public SuccessCode registerUserInfo(@RequestPart(value = "registerUserReq") RegisterUserReq req,
                                         @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException {
        userUseCase.registerProfile(profile);
        return userUseCase.registerUserInfo(req);
    }

    @Operation(summary = "마이페이지에서 닉네임 및 프로필 수정", description = "회원정보 업데이트 api")
    @PatchMapping("/mypage")
    public SuccessCode updateUserInfo(@RequestPart(value="nickname") String nickname,
                                      @RequestPart(value="profile", required = false) MultipartFile profile ) throws IOException {
        return userUseCase.updateUserInfo(nickname, profile);
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
    @GetMapping("/nickname-validation")
    public GetNicknameValidationResp isValid(@RequestParam String nickname){
        return userUseCase.getNicknameValidation(nickname);
    }

    @GetMapping("/{nickname}")
    public Slice<ReadLineReviewResp> getUserLineReviews(
            @PathVariable String nickname,
            @Parameter(description = "0 < size <= 10") @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long lastReviewId) {

        UserLineReviewsReq req = new UserLineReviewsReq(nickname, lastReviewId);

        return userUseCase.getLineReviewsByNickname(PageRequest.ofSize(size), req);
    }



}