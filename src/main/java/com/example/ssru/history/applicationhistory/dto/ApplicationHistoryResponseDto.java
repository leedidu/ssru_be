package com.example.ssru.history.applicationhistory.dto;

import com.example.ssru.coupon.dto.CouponResponseDto;
import com.example.ssru.coupon.entity.Coupon;
import com.example.ssru.option.dto.OptionResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

public class ApplicationHistoryResponseDto {
    @Getter
    @AllArgsConstructor
    public static class Response{
        private String status;
        private ApplicationResponse application;
    }

    @Getter
    @AllArgsConstructor
    public static class ApplicationResponse{
        private int id;
    }

    @Getter
    @AllArgsConstructor
    public static class ApplicationHistoryDetail{
        private int id;
        private String progress;
        private String address;
        private String detailAddress;
        private int price;
        private String photo;
        private Timestamp created;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ApplicationHistoryDetailById{
        private int id;
        private int price;
        private String address;
        private String detailAddress;
        private String photo;
        private String comment;
        private Timestamp created;
        private List<CouponResponseDto.CouponDetail> coupons;
        private List<OptionResponseDto.HistoryOptionResponse> options;
    }
}
