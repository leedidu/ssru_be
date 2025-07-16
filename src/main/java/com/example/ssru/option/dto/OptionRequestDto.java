package com.example.ssru.option.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

public class OptionRequestDto {
    @Getter
    @AllArgsConstructor
    public static class Post {
        private String type;
        private String detail;
        private int count;
        private int price;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Patch{
        private int id;
        private String type;
        private String detail;
        private int count;
        private int price;
    }
}
