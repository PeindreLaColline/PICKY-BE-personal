package com.ureca.picky_be.base.presentation.controller.auth;

import com.ureca.picky_be.base.business.auth.OAuth2UseCaseResolver;
import com.ureca.picky_be.base.business.auth.dto.LoginUserReq;
import org.springframework.web.bind.annotation.*;

import com.ureca.picky_be.base.business.auth.OAuth2UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
public class OAuth2Controller {
    private final OAuth2UseCaseResolver oAuth2UseCaseResolver;

    @GetMapping("/{platform}/login")
    public ResponseEntity<String> getOAuth2LoginUrl (@PathVariable String platform) {
        OAuth2UseCase oAuth2UseCase = oAuth2UseCaseResolver.resolve(platform);
        return ResponseEntity.ok(oAuth2UseCase.getLoginUrl());
    }

    @PostMapping("/{platform}/user")
    public ResponseEntity<Void> getUserInfo(@PathVariable String platform, @RequestBody LoginUserReq req){
        OAuth2UseCase oAuth2UseCase = oAuth2UseCaseResolver.resolve(platform);
        oAuth2UseCase.getUserInfo(req);
        return ResponseEntity.ok().build();
    }
}