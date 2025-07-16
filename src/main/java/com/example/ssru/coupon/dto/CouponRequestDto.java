package com.example.ssru.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

public class CouponRequestDto {
    @Getter
    @AllArgsConstructor
    public static class Post {
        private String couponName;
        private int discount;
        private Timestamp publish;
        private Timestamp deadline;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Patch{
        private int id;
        private Boolean whetherUse;
        private String couponName;
        private int discount;
        private Timestamp publish;
        private Timestamp deadline;
    }
}
