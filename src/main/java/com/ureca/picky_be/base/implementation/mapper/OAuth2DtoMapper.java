package com.ureca.picky_be.base.implementation.mapper;

import com.ureca.picky_be.base.business.auth.dto.OAuth2Token;
import com.ureca.picky_be.base.business.auth.dto.TokenResp;
import com.ureca.picky_be.global.web.LocalJwtDto;
import org.springframework.stereotype.Component;

@Component
public class OAuth2DtoMapper {
    public TokenResp toTokenResp(OAuth2Token oAuth2Token, LocalJwtDto localJwtDto, boolean isRegistrationDone, String role) {
        return new TokenResp(
                oAuth2Token,
                localJwtDto,
                isRegistrationDone,
                role
        );
    }
}
