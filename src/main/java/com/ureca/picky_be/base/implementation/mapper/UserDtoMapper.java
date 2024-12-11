package com.ureca.picky_be.base.implementation.mapper;

import com.ureca.picky_be.base.business.user.dto.GetNicknameValidationResp;
import com.ureca.picky_be.base.business.user.dto.GetUserResp;
import com.ureca.picky_be.jpa.genre.Genre;
import com.ureca.picky_be.jpa.user.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDtoMapper {
    public GetUserResp toGetUserResp(User user, String profileUrl) {
        return new GetUserResp(
                user.getName(),
                user.getNickname(),
                user.getBirthdate(),
                user.getGender(),
                user.getNationality(),
                user.getEmail(),
                profileUrl
        );
    }

    public GetNicknameValidationResp toGetNicknameValidationResp(boolean isValid) {
        return new GetNicknameValidationResp(isValid);
    }
}