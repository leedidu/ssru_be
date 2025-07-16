package com.example.ssru.exception;

import lombok.Getter;

public class CustomException extends RuntimeException{
    @Getter
    private CustomExceptionCode customExceptionCode;

    public CustomException(CustomExceptionCode customExceptionCode){
        super(customExceptionCode.getMessage());
        this.customExceptionCode = customExceptionCode;
    }
}
