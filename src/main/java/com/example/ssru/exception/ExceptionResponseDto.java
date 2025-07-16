package com.example.ssru.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ExceptionResponseDto {
    @Getter
    @AllArgsConstructor
    public static class Response{
        private String status;
        private int code;
        private String message;
    }
}