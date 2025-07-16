package com.example.ssru.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class PaymentRequestDto {
    @Getter
    @AllArgsConstructor
    public static class prepareCheck{
        private String merchantUid;
        private int amount;
    }

    @Getter
    @AllArgsConstructor
    public static class savePayment{
        private int amount;
    }
}