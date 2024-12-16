package com.ureca.picky_be.base.presentation.controller.email;

import com.ureca.picky_be.base.business.email.MailUseCase;
import com.ureca.picky_be.base.business.email.dto.EventMessageReq;
import com.ureca.picky_be.global.success.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/email")
public class EmailController {

    private final MailUseCase mailUseCase;

    @Operation(summary = "이벤트 이메일 전송 API", description = "특정 이벤트에 대한 이메일")
    @PostMapping("/event")
    public SuccessCode sendEventEmail(
            @Parameter(description = "보내려하는 사람의 id")
            @RequestParam Long userId,
            @RequestBody EventMessageReq req
    )
    {
        mailUseCase.sendEventEmail(userId, req);
        return SuccessCode.EMAIL_SEND_SUCCESS;
    }

    @Operation(summary = "회원가입 이메일 전송 API", description = "회원가입시 환영을 위한 이메일")
    @PostMapping("/register")
    public SuccessCode sendRegisterCongratulationsEmail(
            @Parameter(description = "보내려하는 사람의 id")
            @RequestParam Long userId)
    {
        mailUseCase.sendRegisterCongratulationMail(userId);
        return SuccessCode.EMAIL_SEND_SUCCESS;
    }

}
