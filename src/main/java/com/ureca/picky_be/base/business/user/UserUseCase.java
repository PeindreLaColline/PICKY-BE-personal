package com.ureca.picky_be.base.business.user;

import com.ureca.picky_be.base.business.user.dto.*;
import com.ureca.picky_be.global.success.SuccessCode;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserUseCase {
    SuccessCode registerUserInfo(RegisterUserReq req);
    SuccessCode registerProfile(MultipartFile profile) throws IOException;
    SuccessCode updateUserInfo(String nickname, MultipartFile profile) throws IOException;
    GetUserResp getUserInfo();
    GetNicknameValidationResp getNicknameValidation(String nickname);

    GetMyPageUserInfoResp getMyPageUserInfo(String nickname);

    List<GetSearchUsersResp> getSearchUsers(String keyword);
}
