package com.ureca.picky_be.base.business.email;

import com.ureca.picky_be.base.business.email.dto.EventMessageReq;
import com.ureca.picky_be.base.implementation.auth.AuthManager;
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
    private final AuthManager authManager;

    @Override
    public void sendEventEmail(EventMessageReq req) {
        Long userId = authManager.getUserId();
        String email = userManager.getUserEmailById(userId);
        emailManager.sendOneEmail(email, req);
    }

    @Override
    public void sendRegisterCongratulationMail() {
//        String email = userManager.getUserEmailById(userId);
        Long userId = authManager.getUserId();
        User user = userManager.getUserById(userId);
        emailManager.sendRegisterContratulateEmail(user);
    }
}
