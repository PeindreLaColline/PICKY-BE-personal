package com.ureca.picky_be.base.business.auth;

import com.ureca.picky_be.base.business.auth.dto.LoginUserReq;

public interface OAuth2UseCase {

    String getLoginUrl();
    String getUserInfo(LoginUserReq req);

}
