package com.ureca.picky_be.base.business.follow.dto;

public record GetFollowUserResp(
        Long userId,
        String userNickname,
        String userProfileUrl,
        String userRole
) {
}
