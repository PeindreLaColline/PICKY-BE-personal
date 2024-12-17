package com.ureca.picky_be.base.business.user.dto;

public record GetMyPageUserInfoResp(
        Long userId,
        String userProfileUrl,
        String userNickname,
        String userRole,
        Integer boardCount,
        Integer followerCount,
        Integer followingCount,
        boolean isFollowing
) {
}
