package com.ureca.picky_be.base.business.auth;

import com.ureca.picky_be.base.business.auth.dto.*;
import com.ureca.picky_be.base.implementation.auth.AuthManager;
import com.ureca.picky_be.base.implementation.auth.KakaoManager;
import com.ureca.picky_be.base.implementation.mapper.OAuth2DtoMapper;
import com.ureca.picky_be.global.success.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoService implements OAuth2UseCase {

    private final KakaoManager kakaoManager;
    private final AuthManager authManager;
    private final OAuth2DtoMapper oAuth2DtoMapper;

    //TODO: state 랜덤 생성 및 유효성 검사
    private String state="1234";

    @Override
    public LoginUrlResp getLoginUrl() {
        return kakaoManager.buildCodeUrl(state);
    }

    @Override
    public TokenResp sendJwtToken(String state, String code) {
        OAuth2Token oAuth2Token = kakaoManager.getOAuth2Token(code);
        String email = kakaoManager.getUserInfo(oAuth2Token.accessToken());
        LoginUserInfo loginUserInfo = kakaoManager.getLocalJwt(email, oAuth2Token.accessToken());
        System.out.println(loginUserInfo);
        System.out.println(oAuth2Token);
        return oAuth2DtoMapper.toTokenResp(oAuth2Token, loginUserInfo.jwt(), kakaoManager.isRegistrationDone(loginUserInfo.userId()));
    }

    @Override
    public SuccessCode deleteAccount(DeleteUserReq req) {
        return kakaoManager.deleteAccount(authManager.getUserId(), req);
    }
}
