package com.example.ssru.option.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class OptionResponseDto {
    @Getter
    @AllArgsConstructor
    public static class Response{
        private int id;
    }

    @Getter
    @AllArgsConstructor
    public static class OptionDetailResponse{
        private int id;
        private String type;
        private String detail;
        private int count;
        private int price;
        private String progress;
    }

    @Getter
    @AllArgsConstructor
    public static class HistoryOptionResponse{
        private String type;
        private String detail;
        private int count;
    }
}
