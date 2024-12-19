package com.ureca.picky_be.base.business.user.dto;

public record BoardQueryReq(
        String nickname,
        Long lastBoardId
) {
}
