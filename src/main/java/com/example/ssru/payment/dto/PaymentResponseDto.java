package com.example.ssru.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class PaymentResponseDto {
    @Getter
    @AllArgsConstructor
    public static class Response{
        private String status;
    }
}
