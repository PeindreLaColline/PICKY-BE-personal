package com.ureca.picky_be.base.business.user;

import com.ureca.picky_be.base.business.user.dto.GetNicknameValidationResp;
import com.ureca.picky_be.base.business.user.dto.GetUserResp;
import com.ureca.picky_be.base.business.user.dto.UpdateUserReq;
import com.ureca.picky_be.base.implementation.auth.AuthManager;
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
    private final AuthManager authManager;

    @Override
    public SuccessCode updateUserInfo(UpdateUserReq req) {
        return userManager.updateUserInfo(authManager.getUserId(), req);
    }

    @Override
    public GetUserResp getUserInfo() {
        User user = userManager.getUserInfo(authManager.getUserId());
        List<Genre> genres = userManager.getUserGenrePreference(user.getId());
        return userDtoMapper.toGetUserResp(user, genres);
    }

    @Override
    public GetNicknameValidationResp getNicknameValidation(String nickname){
        return userDtoMapper.toGetNicknameValidationResp(userManager.getNicknameValidation(nickname));
    }
}