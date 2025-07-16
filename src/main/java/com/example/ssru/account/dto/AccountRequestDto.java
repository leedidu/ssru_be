package com.example.ssru.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.sql.Timestamp;

public class AccountRequestDto {
    @Getter
    @AllArgsConstructor
    public static class Post{
        private String bank;
        private String account;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Patch{
        private int id;
        private String bank;
        private String account;
        private String name;
    }
}
