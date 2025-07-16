package com.example.ssru.history.applicationhistory.dto;

import com.example.ssru.option.dto.OptionRequestDto;
import com.example.ssru.option.entity.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.util.List;

public class ApplicationHistoryRequestDto {
    @Getter
    @AllArgsConstructor
    public static class Post {
        private List<OptionRequestDto.Post> option;
        private String address;
        private String detailAddress;
        private String comment;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Patch{
        private int id;
        private String progress;
        private String address;
        private String detailAddress;
        private String photo;
        private String comment;
    }
}
