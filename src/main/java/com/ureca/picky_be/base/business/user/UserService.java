package com.ureca.picky_be.base.business.user;

import com.ureca.picky_be.base.business.user.dto.*;
import com.ureca.picky_be.base.implementation.auth.AuthManager;
import com.ureca.picky_be.base.implementation.board.BoardManager;
import com.ureca.picky_be.base.implementation.content.ImageManager;
import com.ureca.picky_be.base.implementation.content.ProfileManager;
import com.ureca.picky_be.base.implementation.follow.FollowManager;
import com.ureca.picky_be.base.implementation.mapper.UserDtoMapper;
import com.ureca.picky_be.base.implementation.user.UserManager;
import com.ureca.picky_be.elasticsearch.document.user.UserDocument;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final UserManager userManager;
    private final UserDtoMapper userDtoMapper;
    private final AuthManager authManager;
    private final ProfileManager profileManager;
    private final ImageManager imageManager;
    private final BoardManager boardManager;
    private final FollowManager followManager;

    @Override
    public SuccessCode registerUserInfo(RegisterUserReq req) {
        User user = userManager.registerUserInfo(authManager.getUserId(), req);
        userManager.addUserElastic(user);
        return SuccessCode.UPDATE_USER_SUCCESS;
    }

    @Override
    public SuccessCode registerProfile(MultipartFile profile) throws IOException {
        return userManager.registerProfile(profile, authManager.getUserId());
    }

    @Override
    public SuccessCode updateUserInfo(String nickname, MultipartFile profile) throws IOException {
        // 1. 닉네임 예외처리
        // 2. 사진 유무

        if(nickname == null || nickname.isEmpty()) {        // 닉네임 비어있는 경우 무조건 에러
            throw new CustomException(ErrorCode.NO_NICKNAME_ENTERED);
        }

        // 닉네임이 현재 사용자랑 동일하면 Pass, 동일하지 않지만 이미 존재하는 경우
        if(!authManager.getUserNickname().equals(nickname) && !userManager.getNicknameValidation(nickname)) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_NICKNAME);
        }


        // TODO : 추후 예외 처리 리팩토링 예정
        // 현재 별도의 메소드들이 내부에 예외처리 되어 있어서 별도의 Try catch 돌려야함
        // 프로필이 null인 경우 => 프로필 변경하지 않는 경우
        if(profile != null) {
            try {
                userManager.registerProfile(profile, authManager.getUserId());
            } catch (Exception e) {
                throw new CustomException(ErrorCode.USER_PROFILE_UPDATE_FAILED);
            }
        }

        try {
            userManager.updateUserNickname(authManager.getUserId(), nickname);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.USER_UPDATE_FAILED);
        }
        return SuccessCode.UPDATE_USER_SUCCESS;
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public GetMyPageUserInfoResp getMyPageUserInfo(String nickname) {
        Long currentUserId = authManager.getUserId();
        Long userId = userManager.getUserIdByNickname(nickname);
        UserInfoProjection proj = userManager.getUserInfoById(userId);
        String profileUrl = profileManager.getPresignedUrl(proj.getProfileUrl());

        Integer boardCount = boardManager.getUserBoardCount(userId);
        boolean isFollowing = followManager.checkFollowing(currentUserId, userId);
        // 여기에 isFollowing(해당 사용자를 팔로잉 하고 있는지 없는지)
        // 본인이면 예외 처리

        Integer followerCount = userManager.getUserFollowingCount(userId);
        Integer followingCount = userManager.getUserFollowerCount(userId);
        return new GetMyPageUserInfoResp(userId, profileUrl, proj.getNickname(), proj.getRole(), boardCount, followerCount, followingCount, isFollowing);
    }

    @Override
    public List<GetSearchUsersResp> getSearchUsers(String keyword) {
        List<UserDocument> users = userManager.getSearchUsers(keyword);
        return userDtoMapper.toGetSearchUsers(users);
    }
}
