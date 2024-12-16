package com.ureca.picky_be.base.business.email;

import com.ureca.picky_be.base.business.email.dto.EventMessageReq;
import com.ureca.picky_be.base.implementation.email.EmailManager;
import com.ureca.picky_be.base.implementation.user.UserManager;
import com.ureca.picky_be.jpa.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService implements MailUseCase {

    private final EmailManager emailManager;
    private final UserManager userManager;

    @Override
    public void sendEventEmail(Long userId, EventMessageReq req) {
        String email = userManager.getUserEmailById(userId);
        emailManager.sendOneEmail(email, req);
    }

    @Override
    public void sendRegisterCongratulationMail(Long userId) {
//        String email = userManager.getUserEmailById(userId);
        User user = userManager.getUserById(userId);
        emailManager.sendRegisterContratulateEmail(user);
    }
}
