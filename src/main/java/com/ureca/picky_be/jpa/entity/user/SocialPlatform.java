package com.ureca.picky_be.jpa.entity.user;


import lombok.Getter;

@Getter
public enum SocialPlatform {
    NAVER("naver"),
    KAKAO("kakao"),
    GOOGLE("google");

    private final String value;

    SocialPlatform(String value) {
        this.value = value;
    }
}
