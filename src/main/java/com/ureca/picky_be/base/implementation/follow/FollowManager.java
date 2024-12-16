package com.ureca.picky_be.base.implementation.follow;

import com.ureca.picky_be.base.business.follow.dto.FollowProjection;
import com.ureca.picky_be.base.persistence.follow.FollowRepository;
import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.entity.follow.Follow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class FollowManager {

    private final FollowRepository followRepository;

    public SuccessCode manageFollowingRelationship(Long followerId, Long followingId) {
        validateSameUser(followerId, followingId);
        Optional<Follow> rel = followRepository.findByFollowerIdAndFollowingId(followerId, followingId);

        if(rel.isPresent()) {  // 이미 팔로잉을 한 경우
            followRepository.deleteFollowByFollowerIdAndFollowingId(rel.get().getFollowerId(), rel.get().getFollowingId());
            return SuccessCode.DELETE_FOLLOW_SUCCESS;
        } else {
            Follow follow = Follow.of(followerId, followingId);
            followRepository.save(follow);
            return SuccessCode.CREATE_FOLLOW_SUCCESS;
        }
    }

    private void validateSameUser(Long followerId, Long followingId) {
        // 1. 같은 사람인지
        // 2. 이미 팔로잉하고 있는 사람인지
        if(followerId.equals(followingId)) {
            throw new CustomException(ErrorCode.FOLLOW_SAME_USER);
        }
    }

    private boolean existsFollowingRelationship(Long followerId, Long followingId) {
        Optional<Follow> rel = followRepository.findByFollowerIdAndFollowingId(followerId, followingId);
        return rel.isPresent();
    }

    public Slice<FollowProjection> findFollowersByUserId(Long userId, PageRequest pageRequest) {
        return followRepository.findFollowersByFollowingId(userId, pageRequest);
    }

    public Slice<FollowProjection> findFollowingsByUserId(Long userId, PageRequest pageRequest) {
        return followRepository.findFollowingsByFollowerId(userId, pageRequest);
    }
}
