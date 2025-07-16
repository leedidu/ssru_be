package com.example.ssru.pointhistory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class PointHistoryResponseDto {
    @Getter
    @AllArgsConstructor
    public static class Response{
    }

    @Getter
    @AllArgsConstructor
    public static class PointHistoryListResponse{
        private int id;
        private LocalDateTime created;
        private String situation;
        private String method;
        private int amount;
    }
}
