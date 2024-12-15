package com.ureca.picky_be.base.business.email;

import com.ureca.picky_be.base.implementation.email.EmailManager;
import com.ureca.picky_be.base.implementation.user.UserManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService implements MailUseCase {

    private final EmailManager emailManager;
    private final UserManager userManager;

    @Override
    public void createEmailAndSendToUser(Long userId) {
        String email = userManager.getUserEmailById(userId);
        emailManager.sendOneEmail(email);
    }
}
