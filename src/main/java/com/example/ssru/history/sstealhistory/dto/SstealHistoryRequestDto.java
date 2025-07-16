package com.example.ssru.history.sstealhistory.dto;

import com.example.ssru.option.entity.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.List;

public class SstealHistoryRequestDto {
    @Getter
    @AllArgsConstructor
    public static class Post{
        private Timestamp estimatedDate;
        private String photo;
        private List<Integer> optionId;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CancelSsteal{
        private String reason;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Patch{
        private int id;
        private Timestamp estimatedDate;
        private String progress;
        private String photo;
    }
}
