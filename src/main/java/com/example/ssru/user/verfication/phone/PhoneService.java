package com.example.ssru.user.verfication.phone;

import com.example.ssru.redis.RedisService;
import com.example.ssru.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
public class PhoneService {
    private static final String DOMAIN = "https://api.coolsms.co.kr";
    // 전화번호 인증 코드 발송시 사용할 텍스트
    private static final String PHONE_AUTH_TEXT = "[쓰루] 인증코드 : %s";
    // 성공 코드

    private final DefaultMessageService messageService;
    private final String sendNumber;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;
    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;
    private static final String AUTH_CODE_PREFIX = "AuthCode ";


    public PhoneService(@Value("${coolsms.apikey}") String key,
                      @Value("${coolsms.apisecret}") String secret,
                      @Value("${coolsms.fromnumber}") String sendNumber) {
        this.messageService = NurigoApp.INSTANCE.initialize(key, secret, DOMAIN);
        this.sendNumber = sendNumber;
    }

    // 메시지 생성 및 설정
    private Message createMessage(String toNumber, String code) {

        Message message = new Message();

        message.setFrom(sendNumber);
        message.setTo(toNumber);
        message.setText(String.format(PHONE_AUTH_TEXT, code));

        return message;
    }

    // sms 전송
    public void sendSms(String phone) {
        // 랜덤 코드 생성
        String code = userService.createCode();
        Message message = createMessage(phone, code);
        SingleMessageSentResponse response = this.messageService.sendOne(
                new SingleMessageSendingRequest(message));
        redisService.setValues(AUTH_CODE_PREFIX + phone, code, Duration.ofMillis(this.authCodeExpirationMillis));
    }
}
