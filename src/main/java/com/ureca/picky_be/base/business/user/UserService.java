package com.ureca.picky_be.base.business.user;

import com.ureca.picky_be.base.business.user.dto.*;
import com.ureca.picky_be.base.implementation.auth.AuthManager;
import com.ureca.picky_be.base.implementation.board.BoardManager;
import com.ureca.picky_be.base.implementation.content.ImageManager;
import com.ureca.picky_be.base.implementation.content.ProfileManager;
import com.ureca.picky_be.base.implementation.mapper.UserDtoMapper;
import com.ureca.picky_be.base.implementation.user.UserManager;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final UserManager userManager;
    private final UserDtoMapper userDtoMapper;
    private final AuthManager authManager;
    private final ProfileManager profileManager;
    private final ImageManager imageManager;
    private final BoardManager boardManager;

    @Override
    public SuccessCode registerUserInfo(RegisterUserReq req) {
        return userManager.registerUserInfo(authManager.getUserId(), req);
    }

    @Override
    public SuccessCode registerProfile(MultipartFile profile) throws IOException {
        return userManager.registerProfile(profile, authManager.getUserId());
    }

    @Override
    public SuccessCode updateUserInfo(String nickname, MultipartFile profile) throws IOException {
        if(nickname==null && profile==null) {
            throw new CustomException(ErrorCode.NO_DATA_RECEIVED);
        }
        if(nickname!=null && !authManager.getUserNickname().equals(nickname)) {
            userManager.updateUserNickname(authManager.getUserId(), nickname);
        }
        userManager.registerProfile(profile, authManager.getUserId());
        return SuccessCode.UPDATE_USER_SUCCESS;
    }

    @Override
    public GetUserResp getUserInfo() {
        User user = userManager.getUserInfo(authManager.getUserId());
        if(user.getProfileUrl() == null) {
            return userDtoMapper.toGetUserResp(user, null);
        } else{
            return userDtoMapper.toGetUserResp(user, imageManager.getPresignedUrl(user.getProfileUrl()));
        }
    }

    @Override
    public GetNicknameValidationResp getNicknameValidation(String nickname){
        return userDtoMapper.toGetNicknameValidationResp(userManager.getNicknameValidation(nickname));
    }

    @Override
    public GetMyPageUserInfoResp getMyPageUserInfo(String nickname) {
        Long userId = userManager.getUserIdByNickname(nickname);
        UserInfoProjection proj = userManager.getUserInfoById(userId);
        String profileUrl = profileManager.getPresignedUrl(proj.getProfileUrl());

        Integer boardCount = boardManager.getUserBoardCount(userId);
        Integer followerCount = userManager.getUserFollowerCount(userId);
        Integer followingCount = userManager.getUserFollowingCount(userId);
        return new GetMyPageUserInfoResp(userId, profileUrl, proj.getNickname(), proj.getRole(), boardCount, followerCount, followingCount);
    }
}
