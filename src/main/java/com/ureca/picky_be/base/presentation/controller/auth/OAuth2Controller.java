package com.ureca.picky_be.base.presentation.controller.auth;

import com.ureca.picky_be.base.business.auth.OAuth2UseCase;
import com.ureca.picky_be.base.business.auth.OAuth2UseCaseResolver;
import com.ureca.picky_be.base.business.auth.dto.DeleteUserReq;
import com.ureca.picky_be.base.business.auth.dto.LoginUrlResp;
import com.ureca.picky_be.base.business.auth.dto.TokenResp;
import com.ureca.picky_be.global.success.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
public class OAuth2Controller {
    private final OAuth2UseCaseResolver oAuth2UseCaseResolver;

    @Operation(summary = "로그인 및 회원가입 url 발급", description = "로그인 및 회원가입에 사용되는 url 반환")
    @GetMapping("/{platform}/login")
    public LoginUrlResp getOAuth2LoginUrl (@PathVariable String platform) {
        OAuth2UseCase oAuth2UseCase = oAuth2UseCaseResolver.resolve(platform);
        return oAuth2UseCase.getLoginUrl();
    }

    @Operation(summary = "프론트에서 code 받아서 토큰 반환", description = "localToken, socialToken, isRegistrationDone return 됨 확인 필!")
    @GetMapping("/{platform}/user")
    public TokenResp getToken(@PathVariable String platform,
                              @RequestParam String code,
                              @RequestParam String state
                                            ){
        OAuth2UseCase oAuth2UseCase = oAuth2UseCaseResolver.resolve(platform);
        System.out.println("와 끝났다!");
        return oAuth2UseCase.sendJwtToken(state, code);
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴")
    @DeleteMapping("/{platform}/user")
    public SuccessCode deleteUser(@PathVariable String platform,
                                  @RequestBody DeleteUserReq req) {
        OAuth2UseCase oAuth2UseCase = oAuth2UseCaseResolver.resolve(platform);
        return oAuth2UseCase.deleteAccount(req);
    }
}
