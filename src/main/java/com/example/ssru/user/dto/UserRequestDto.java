package com.example.ssru.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

public class UserRequestDto {
    @Getter
    @AllArgsConstructor
    public static class Post{
        private String loginId;
        private String loginPw;
        private String name;
        @Pattern(regexp = "^010-?([0-9]{4})-?([0-9]{4})$",
                message = "전화번호 형태는 010-1234-1234 입니다.")
        private String phone;
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$",
                message = "이메일 형식이 올바르지 않습니다.")
        private String email;
        private String nickname;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Patch{
        private int id;
        private String loginPw;
        private String phone;
        private String email;
        private String nickname;
        private String billPw;
    }

    @Getter
    @AllArgsConstructor
    public static class Login{
        private String loginId;
        private String loginPw;
    }
}
