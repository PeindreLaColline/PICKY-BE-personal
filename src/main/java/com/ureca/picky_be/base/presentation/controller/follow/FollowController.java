package com.ureca.picky_be.base.presentation.controller.follow;

import com.ureca.picky_be.base.business.board.dto.commentDto.GetAllBoardCommentsResp;
import com.ureca.picky_be.base.business.follow.FollowUseCase;
import com.ureca.picky_be.base.business.follow.dto.GetFollowUserResp;
import com.ureca.picky_be.global.success.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/follow")
public class FollowController {
    private final FollowUseCase followUseCase;

    @PostMapping
    @Operation(summary = "팔로우 신청/삭제 API", description = "현재 토큰 사용자가 Following(팔로우 할 사람)을 하는 API입니다. 현재 팔로우를 하고 있다면 팔로우 취소(삭제)가 됩니다.")
    public SuccessCode manageFollowing(
            @RequestParam Long followingId
    ) {
        return followUseCase.manageFollowing(followingId);
    }

    @GetMapping("/followers/{nickname}")
    @Operation(summary = "팔로워들 조회 API(무한 스크롤)", description = "현재 사용자를 팔로우하는 사람들을 조회하는 API입니다.")
    public Slice<GetFollowUserResp> getUserFollowers (
            @PathVariable String nickname,
            @Parameter(description = "0 < size <= 10") @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(required = false) Long lastFollowerId) {
        return followUseCase.getFollowers(PageRequest.ofSize(size), nickname, lastFollowerId);
    }

    @GetMapping("/followings/{nickname}")
    @Operation(summary = "팔로잉들 조회 API(무한 스크롤)", description = "현재 사용자가 팔로잉하는 사람들을 조회하는 API입니다.")
    public Slice<GetFollowUserResp> getUserFollowings (
            @PathVariable String nickname,
            @Parameter(description = "0 < size <= 10") @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(required = false) Long lastFollowingId) {
        return followUseCase.getFollowings(PageRequest.ofSize(size), nickname, lastFollowingId);
    }






}
