package com.example.ssru.user.dto;

import com.example.ssru.exception.CustomExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class UserResponseDto {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response{
        private String status;
        private UserResponse user;
    }

    @Getter
    @AllArgsConstructor
    public static class UserResponse{
        private int id;
        private String loginId;
        private String name;
        private String auth;
        private String phone;
        private String email;
        private int point;
        private String nickname;
    }

    @Getter
    @AllArgsConstructor
    public static class DuplicationResponse{
        private Boolean duplication;
    }

    @Getter
    @AllArgsConstructor
    public static class VerifyCheck{
        private String status;
    }
}
