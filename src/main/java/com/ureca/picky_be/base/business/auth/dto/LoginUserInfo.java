package com.ureca.picky_be.base.business.auth.dto;

import com.ureca.picky_be.global.web.LocalJwtDto;

public record LoginUserInfo(LocalJwtDto jwt,
                            Long userId) {
}
