package com.example.ssru.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AccountResponseDto {
    @Getter
    @AllArgsConstructor
    public static class Response{
        private String status;
    }

    @Getter
    @AllArgsConstructor
    public static class checkResponse{
        private boolean result;
    }

    @Getter
    @AllArgsConstructor
    public static class accountListResponse{
        private int id;
        private String bank;
        private String account;
    }
}
