package com.example.ssru.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CouponResponseDto {
    @Getter
    @AllArgsConstructor
    public static class Response{
        private int id;
    }

    @Getter
    @AllArgsConstructor
    public static class CouponDetail{
        private int id;
        private String couponName;
        private int discount;
    }
}
