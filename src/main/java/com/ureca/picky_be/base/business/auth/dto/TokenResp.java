package com.ureca.picky_be.base.business.auth.dto;

import com.ureca.picky_be.global.web.LocalJwtDto;

public record TokenResp(OAuth2Token oAuth2Token,
                        LocalJwtDto localJwtDto,
                        boolean isRegistrationDone) {
}
