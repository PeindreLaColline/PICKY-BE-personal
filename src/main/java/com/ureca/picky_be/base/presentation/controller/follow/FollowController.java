package com.ureca.picky_be.base.presentation.controller.follow;

import com.ureca.picky_be.base.business.follow.FollowUseCase;
import com.ureca.picky_be.global.success.SuccessCode;
import com.ureca.picky_be.jpa.entity.notification.NotificationType;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/follow")
public class FollowController {
    private final FollowUseCase followUseCase;

    @PostMapping()
    @Operation(summary = "팔로우 신청/삭제 API", description = "현재 토큰 사용자가 Following(팔로우 할 사람)을 하는 API입니다. 현재 팔로우를 하고 있다면 팔로우 취소(삭제)가 됩니다.")
    public SuccessCode sendAll(
            @RequestParam Long followingId
    ) {
        return followUseCase.createFollow(followingId);
    }
}
