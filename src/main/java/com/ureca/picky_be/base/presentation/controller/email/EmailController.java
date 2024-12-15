package com.ureca.picky_be.base.presentation.controller.email;

import com.ureca.picky_be.base.business.email.MailUseCase;
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

    @Operation(summary = "이메일 전송 API", description = "아직 어떤 용도로 전송할 것인지 정하지 못함")
    @PostMapping
    public SuccessCode sendEmail(
            @Parameter(description = "보내려하는 사람의 id")
            @RequestParam Long userId)
    {
        mailUseCase.createEmailAndSendToUser(userId);
        return SuccessCode.EMAIL_SEND_SUCCESS;
    }
}
