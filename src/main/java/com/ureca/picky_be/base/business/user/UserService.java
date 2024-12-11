package com.ureca.picky_be.base.business.user;

import com.ureca.picky_be.base.business.user.dto.GetNicknameValidationResp;
import com.ureca.picky_be.base.business.user.dto.GetUserResp;
import com.ureca.picky_be.base.business.user.dto.RegisterUserReq;
import com.ureca.picky_be.base.implementation.auth.AuthManager;
import com.ureca.picky_be.base.implementation.content.ImageManager;
import com.ureca.picky_be.base.implementation.mapper.UserDtoMapper;
import com.ureca.picky_be.base.implementation.user.UserManager;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.genre.Genre;
import com.ureca.picky_be.jpa.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final UserManager userManager;
    private final UserDtoMapper userDtoMapper;
    private final AuthManager authManager;
    private final ImageManager imageManager;

    @Override
    public SuccessCode registerUserInfo(RegisterUserReq req) {
        return userManager.registerUserInfo(authManager.getUserId(), req);
    }

    @Override
    public SuccessCode registerProfile(MultipartFile profile) throws IOException {
        return userManager.registerProfile(profile, authManager.getUserId());
    }

    @Override
    public GetUserResp getUserInfo() {
        User user = userManager.getUserInfo(authManager.getUserId());
        return userDtoMapper.toGetUserResp(user, imageManager.getPresignedUrl(user.getProfileUrl()));
    }

    @Override
    public GetNicknameValidationResp getNicknameValidation(String nickname){
        return userDtoMapper.toGetNicknameValidationResp(userManager.getNicknameValidation(nickname));
    }
}