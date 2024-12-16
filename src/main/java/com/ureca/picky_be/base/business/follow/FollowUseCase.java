package com.ureca.picky_be.base.business.follow;

import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.entity.follow.Follow;

public interface FollowUseCase {

    SuccessCode createFollow(Long followingId);

}
