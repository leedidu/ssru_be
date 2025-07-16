package com.example.ssru.address.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class AddressRequestDto {
    @Getter
    @AllArgsConstructor
    public static class Post {
        private String address;
        private String detailAddress;
        private String type;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Patch{
        private String address;
        private String detailAddress;
        private String type;
    }
}
