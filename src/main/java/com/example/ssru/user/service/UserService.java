package com.example.ssru.user.service;

import com.example.ssru.exception.CustomException;
import com.example.ssru.exception.CustomExceptionCode;
import com.example.ssru.redis.RedisService;
import com.example.ssru.security.jwt.JwtToken;
import com.example.ssru.security.jwt.TokenProvider;
import com.example.ssru.user.entity.User;
import com.example.ssru.user.repository.UserRepository;
import com.example.ssru.user.verfication.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final EmailService emailService;
    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;
    private final RedisService redisService;

    public User createUser(User user){
        System.out.println("----- 회원가입 시작 -----");
        if(!(checkDuplicateEmail(user.getEmail()) && checkDuplicatePhone(user.getPhone()) && checkDuplicateLoginId(user.getLoginId()) && checkDuplicateNickname(user.getNickname()))){
            System.out.println("----- 이메일/전화번호/아이디/닉네임 중복 체크 완료 -----");
            user.setLoginPw(passwordEncoder.encode(user.getLoginPw()));
            System.out.println("----- 회원가입 성공 -----");
            return userRepository.save(user);
        } else{
            System.out.println("----- 회원가입 실패 -----");
            System.out.println("사유 : 이메일/전화번호/아이디/닉네임 중복된 게 있음");
            throw new CustomException(CustomExceptionCode.SIGN_IN_FAIL);
        }
    }

    public Boolean checkDuplicateLoginId(String id){ return userRepository.existsByLoginId(id); }

    public Boolean checkDuplicateEmail(String email){ return userRepository.existsByEmail(email); }

    public Boolean checkDuplicatePhone(String phone){ return userRepository.existsByPhone(phone); }

    public Boolean checkDuplicateNickname(String nickname){ return userRepository.existsByNickname(nickname); }

    public User findUserByUserId(int userId){
        return verifiedUser(userId);
    }

    public void sendCodeToEmail(String toEmail) {
        System.out.println("----- 이메일 보내기 시작 -----");
        boolean check = checkDuplicateEmail(toEmail);
        System.out.println("이메일 중복 확인 : " + check);
        if(!check){
            String title = "쓰루 이메일 확인 메일";
            String authCode = this.createCode();
            String text = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <title>Email Styles</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <p style=\"font-size: 32px; color: #333; font-weight: bold;\">쓰루 이메일 확인 메일입니다.</p>\n" +
                    "    <p style=\"font-size: 20px; color: #333; font-weight: bold;\">앱 화면으로 돌아가 아래 코드 6자리를 입력해주세요.</p>\n" +
                    "    <p style=\"font-size: 32px; color: #5192AC;\">code : " + authCode + "</p>\n" +
                    "    <p style=\"font-style: italic;\">10분 이내에 인증을 완료해야합니다.</p>\n" +
                    "</body>\n" +
                    "</html>\n"; // 이메일 본문
            emailService.sendEmail(toEmail, title, text);
            // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "AuthCode " + Email / value = AuthCode )
            redisService.setValues(AUTH_CODE_PREFIX + toEmail,
                    authCode, Duration.ofMillis(this.authCodeExpirationMillis));
        }
    }

    public String createCode() {
        int lenth = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            System.out.println("인증 번호 : " + builder);
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new CustomException(CustomExceptionCode.EMAIL_VERIFY_FAIL);
        }
    }

    public boolean verifiedCode(String email, String authCode) {
        return redisService.checkExistsValue(redisService.getValues(AUTH_CODE_PREFIX + email)) &&
                redisService.getValues(AUTH_CODE_PREFIX + email).equals(authCode);
    }

    public JwtToken loginUser(String id, String pw){
        System.out.println("----- 로그인 시작 -----");
        User user = userRepository.findByLoginId(id);
        if((user != null) && passwordEncoder.matches(pw, user.getLoginPw())){
            Authentication authentication = new UsernamePasswordAuthenticationToken(id, pw);
            JwtToken token = tokenProvider.createJwtToken(authentication);
            user.setRefreshToken(token.getRefreshToken()); // 데이터베이스에 refresh token 저장 -> 로그인 만료 막기 위해
            userRepository.save(user);
            System.out.println("----- 로그인 성공 -----");
            return token;
        } else{
            System.out.println("----- 로그인 실패 -----");
            return null;
        }
    }

    public int getUserIdByToken(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int userId = 0;
        if(authentication.toString() != null){
            User user = userRepository.findByLoginId(authentication.getName());
            if(user != null){
                userId = user.getId();
            }
        }
        return userId;
    }

    public User getLoginUser(){
        int userId = getUserIdByToken();
        if(userId != 0){
            Optional<User> user = userRepository.findById(userId);
            return user.orElse(null);
        }
        return null;
    }

    public boolean patchUser(User user){
        User user1 = getLoginUser();

        boolean isEmailChanged = Optional.ofNullable(user.getEmail())
                .filter(email -> !email.equals(user1.getEmail()) && !checkDuplicateEmail(email))
                .map(email -> {
                    user1.setEmail(email);
                    return true;
                })
                .orElse(false);

        boolean isPhoneChanged = Optional.ofNullable(user.getPhone())
                .filter(phone -> !phone.equals(user1.getPhone()) && !checkDuplicatePhone(phone))
                .map(phone -> {
                    user1.setPhone(phone);
                    return true;
                })
                .orElse(false);

        boolean isNicknameChanged = Optional.ofNullable(user.getNickname())
                .filter(nickname -> !nickname.equals(user1.getNickname()) && !checkDuplicateNickname(nickname))
                .map(nickname -> {
                    user1.setNickname(nickname);
                    return true;
                })
                .orElse(false);


        if (isEmailChanged || isPhoneChanged || isNicknameChanged) {
            userRepository.save(user1);
            return true;
        } else {
            return false;
        }
    }

    public User verifiedUser(int userId){
        Optional<User> user = userRepository.findById(userId);
        return user.orElseThrow(() -> new CustomException(CustomExceptionCode.USER_NOT_FOUND));
    }

    public void deleteUser(int userId){
        User user = verifiedUser(userId);
        userRepository.delete(user);
    }
}
