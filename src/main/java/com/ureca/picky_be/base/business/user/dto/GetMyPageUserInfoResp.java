package com.ureca.picky_be.base.business.user.dto;

public record GetMyPageUserInfoResp(
        Long userId,
        Integer boardCount,
        Integer followerCount,
        Integer followingCount
) {
}
