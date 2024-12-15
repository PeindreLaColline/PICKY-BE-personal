package com.ureca.picky_be.base.business.email;

public interface MailUseCase {
    void createEmailAndSendToUser(Long userId);
}
