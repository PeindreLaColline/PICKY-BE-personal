package com.ureca.picky_be.base.implementation.email;


import com.ureca.picky_be.global.exception.CustomException;
import com.ureca.picky_be.global.exception.ErrorCode;
import com.ureca.picky_be.jpa.entity.user.User;
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


    @Async("customExecutor")
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


    public void sendRegisterContratulateEmail(User user) {
        String email = user.getEmail();
        if(email == null || email.isEmpty()) {
            throw new CustomException(ErrorCode.USER_EMAIL_EMPTY);
        }


        String name = user.getName();
        if(name == null || name.isEmpty()) {
            throw new CustomException(ErrorCode.USER_NAME_NOT_FOUND);
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();

            // 수신자, 제목, 본문 등 설정
            String subject = "[PICKY] 회원가입 이메일 안내";
            String body = "안녕하세요. " + name + "님!\n\n"
                    + "PICKY 회원가입을 축하합니다.\n\n"
                    + "※유의 사항 안내\n"
                    + "본 이메일은 발신 전용이므로 회신이 되지 않습니다.\n"
                    + "추가 문의사항은 1:1 문의하기 기능을 통해 접수해주시면 감사하겠습니다. : )\n\n"
                    + "감사합니다.\n"
                    + "PICKY 드림";

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body, true);

            // Email 전송
            mailSender.send(mimeMessage);

        } catch (MessagingException e){
            throw new CustomException(ErrorCode.EMAIL_SEND_FAILED);
        }
    }


}
