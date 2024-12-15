package com.ureca.picky_be.base.implementation.email;


import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailManager {
    private final JavaMailSender mailSender;

    @Async
    public void sendOneEmail(String to) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            // 수신자, 제목, 본문 등 설정
            String subject = "[PICKY] 이벤트 및 이메일 전송 안내";
            String body = "PICKY 이메일 전송 및 이벤트 안내를 위해 발송된 메일입니다.";

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body, true);

            // Email 전송
            mailSender.send(mimeMessage);

        } catch (MessagingException e){
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }


}
