package com.ureca.picky_be.base.business.user;

import com.ureca.picky_be.base.business.user.dto.GetUserResp;
import com.ureca.picky_be.base.business.user.dto.UpdateUserReq;
import com.ureca.picky_be.base.implementation.mapper.UserDtoMapper;
import com.ureca.picky_be.base.implementation.user.UserManager;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.genre.Genre;
import com.ureca.picky_be.jpa.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final UserManager userManager;
    private final UserDtoMapper userDtoMapper;

    @Override
    public SuccessCode updateUserInfo(UpdateUserReq req) {
        return userManager.updateUserInfo(req);
    }

    @Override
    public GetUserResp getUserInfo() {
        User user = userManager.getUserInfo();
        List<Genre> genres = userManager.getUserGenrePreference(user.getId());
        return userDtoMapper.toGetUserResp(user, genres);
    }
}
