package com.ureca.picky_be.base.implementation.mapper;

import com.ureca.picky_be.base.business.user.dto.GetUserResp;
import com.ureca.picky_be.jpa.genre.Genre;
import com.ureca.picky_be.jpa.user.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDtoMapper {
    public GetUserResp toGetUserResp(User user, List<Genre> genres) {
        return new GetUserResp(
                user.getName(),
                user.getNickname(),
                user.getProfileUrl(),
                user.getBirthdate(),
                user.getGender(),
                user.getNationality(),
                genres
        );
    }
}
