package com.example.ssru.account.dto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class IamportResponseDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor // 기본 생성자 추가
    public static class IamPortResponse{
        private int code;
        private String message;
        private IamportResponseBody response;

        @Getter
        public static class IamportResponseBody {
            @JsonProperty("access_token")
            private String accessToken;
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CheckAccountResponse{
        private int code;
        private String message;
        private CheckAccountHolder response;

        @Getter
        public static class CheckAccountHolder{
            @JsonProperty("bank_holder")
            private String bankHolder;
        }
    }
}

