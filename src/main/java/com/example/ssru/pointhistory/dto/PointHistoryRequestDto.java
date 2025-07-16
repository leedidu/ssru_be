package com.example.ssru.pointhistory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

public class PointHistoryRequestDto {
    @Getter
    @AllArgsConstructor
    public static class Post{
        private String method;
        private int amount;
        private String situation;
        private String merchantUid;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Patch{
        private int id;
        private Timestamp created;
        private Timestamp updated;
    }
}
