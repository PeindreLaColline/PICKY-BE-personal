package com.ureca.picky_be.base.business.user;

import com.ureca.picky_be.base.business.lineReview.dto.LineReviewProjection;
import com.ureca.picky_be.base.business.lineReview.dto.ReadLineReviewResp;
import com.ureca.picky_be.base.business.user.dto.GetNicknameValidationResp;
import com.ureca.picky_be.base.business.user.dto.GetUserResp;
import com.ureca.picky_be.base.business.user.dto.RegisterUserReq;
import com.ureca.picky_be.base.business.user.dto.UserLineReviewsReq;
import com.ureca.picky_be.base.implementation.auth.AuthManager;
import com.ureca.picky_be.base.implementation.content.ImageManager;
import com.ureca.picky_be.base.implementation.lineReview.LineReviewManager;
import com.ureca.picky_be.base.implementation.lineReview.mapper.LineReviewDtoMapper;
import com.ureca.picky_be.base.implementation.mapper.UserDtoMapper;
import com.ureca.picky_be.base.implementation.user.UserManager;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserService implements UserUseCase {

    private final UserManager userManager;
    private final UserDtoMapper userDtoMapper;
    private final LineReviewDtoMapper lineReviewDtoMapper;
    private final AuthManager authManager;
    private final ImageManager imageManager;
    private final LineReviewManager lineReviewManager;


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
        userManager.updateUserNickname(authManager.getUserId(), nickname);
        return userManager.registerProfile(profile, authManager.getUserId());
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


}