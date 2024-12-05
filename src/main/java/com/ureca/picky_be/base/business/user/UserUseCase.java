package com.ureca.picky_be.base.business.user;

import com.ureca.picky_be.base.business.user.dto.GetNicknameValidationResp;
import com.ureca.picky_be.base.business.user.dto.GetUserResp;
import com.ureca.picky_be.base.business.user.dto.UpdateUserReq;
import com.ureca.picky_be.global.success.SuccessCode;

public interface UserUseCase {
    SuccessCode updateUserInfo(UpdateUserReq req);
    GetUserResp getUserInfo();
    GetNicknameValidationResp getNicknameValidation(String nickname);
}
