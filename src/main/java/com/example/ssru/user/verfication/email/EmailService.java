package com.example.ssru.user.verfication.email;

import com.example.ssru.exception.CustomException;
import com.example.ssru.exception.CustomExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    public void sendEmail(String toEmail,
                          String title,
                          String text) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setTo(toEmail);
            messageHelper.setSubject(title);
            messageHelper.setText(text, true);
            emailSender.send(message);
        } catch (Exception e) {
            throw new CustomException(CustomExceptionCode.UNABLE_TO_SEND_EMAIL);
        }
    }
}
