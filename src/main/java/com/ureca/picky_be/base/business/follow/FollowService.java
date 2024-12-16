package com.ureca.picky_be.base.business.follow;

import com.ureca.picky_be.base.business.board.dto.BoardProjection;
import com.ureca.picky_be.base.business.follow.dto.FollowProjection;
import com.ureca.picky_be.base.business.follow.dto.GetFollowUserResp;
import com.ureca.picky_be.base.implementation.auth.AuthManager;
import com.ureca.picky_be.base.implementation.content.ImageManager;
import com.ureca.picky_be.base.implementation.content.ProfileManager;
import com.ureca.picky_be.base.implementation.follow.FollowManager;
import com.ureca.picky_be.base.implementation.user.UserManager;
import com.ureca.picky_be.base.persistence.follow.FollowRepository;
import com.ureca.picky_be.global.success.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService implements FollowUseCase{

    private final UserManager userManager;
    private final FollowManager followManager;
    private final AuthManager authManager;
    private final ProfileManager profileManager;

    @Override
    @Transactional
    public SuccessCode manageFollowing(Long followingId) {
        Long userId = authManager.getUserId();
        userManager.validateUserStatus(userId);
        userManager.validateUserStatus(followingId);
        return followManager.manageFollowingRelationship(userId, followingId);

    }

    @Override
    @Transactional(readOnly = true)
    public Slice<GetFollowUserResp> getFollowers(PageRequest pageRequest, String nickname, Long lastFollowerId) {
        Long userId = userManager.getUserIdByNickname(nickname);
        userManager.validateUserStatus(userId);
        Slice<FollowProjection> followers = followManager.findFollowersByUserId(userId, pageRequest);

        // TODO : 리팩토링
        // 지저분하지만 4 계층 규칙으로 인해 profileManager를 여기에서 사용
        return followers.map(follower ->
                new GetFollowUserResp(
                        follower.getUserId(),
                        follower.getUserNickname(),
                        profileManager.getPresignedUrl(follower.getUserProfileUrl()),
                        follower.getUserRole()
                ));
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<GetFollowUserResp> getFollowings(PageRequest pageRequest, String nickname, Long lastFollowingId) {
        Long userId = userManager.getUserIdByNickname(nickname);
        userManager.validateUserStatus(userId);
        Slice<FollowProjection> followings = followManager.findFollowingsByUserId(userId, pageRequest);

        return followings.map(following ->
                new GetFollowUserResp(
                        following.getUserId(),
                        following.getUserNickname(),
                        profileManager.getPresignedUrl(following.getUserProfileUrl()),
                        following.getUserRole()
                ));
    }


}
