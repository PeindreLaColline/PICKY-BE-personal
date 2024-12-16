package com.ureca.picky_be.base.business.follow;

import com.ureca.picky_be.base.implementation.auth.AuthManager;
import com.ureca.picky_be.base.implementation.follow.FollowManager;
import com.ureca.picky_be.base.implementation.user.UserManager;
import com.ureca.picky_be.global.success.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService implements FollowUseCase{

    private final UserManager userManager;
    private final FollowManager followManager;
    private final AuthManager authManager;

    @Override
    @Transactional
    public SuccessCode createFollow(Long followingId) {
        Long userId = authManager.getUserId();
        userManager.validateUserStatus(userId);
        userManager.validateUserStatus(followingId);
        return followManager.manageFollowingRelationship(userId, followingId);
//        return SuccessCode.CREATE_FOLLOW_SUCCESS;
    }


}
