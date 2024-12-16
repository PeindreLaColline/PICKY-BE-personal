package com.ureca.picky_be.base.business.email;

import com.ureca.picky_be.base.business.email.dto.EventMessageReq;

public interface MailUseCase {
    void sendEventEmail(Long userId, EventMessageReq req);

    void sendRegisterCongratulationMail(Long userId);
}
