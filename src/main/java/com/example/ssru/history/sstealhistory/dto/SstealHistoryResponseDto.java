package com.example.ssru.history.sstealhistory.dto;

import com.example.ssru.coupon.dto.CouponResponseDto;
import com.example.ssru.option.dto.OptionResponseDto;
import com.example.ssru.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class SstealHistoryResponseDto {
    @Getter
    @AllArgsConstructor
    public static class Response{
        private String status;
        private SstealDetail sstealDetail;
    }

    @Getter
    @AllArgsConstructor
    public static class SstealDetail{
        private int id;
    }

    @Getter
    @AllArgsConstructor
    public static class CancelResponse{
        private String status;
    }

    @Getter
    @AllArgsConstructor
    public static class SstealHistoryDetail{
        private int id;
        private String progress;
        private int price;
        private String photo;
        private Timestamp created;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SstealHistoryDetailById{
        private int id;
        private String address;
        private String detailAddress;
        private LocalDateTime updated;
        private int price;
        private String photo;
        private List<OptionResponseDto.HistoryOptionResponse> options;
    }
}
