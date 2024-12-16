package com.ureca.picky_be.base.business.follow;

import com.ureca.picky_be.base.business.follow.dto.GetFollowUserResp;
import com.ureca.picky_be.global.success.SuccessCode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

public interface FollowUseCase {

    SuccessCode manageFollowing(Long followingId);

    Slice<GetFollowUserResp> getFollowers(PageRequest pageRequest, String nickname, Long lastFollowerId);

    Slice<GetFollowUserResp> getFollowings(PageRequest pageRequest, String nickname, Long lastFollowingId);
}
