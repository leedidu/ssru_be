package com.example.ssru.payment.dto;

import com.example.ssru.account.dto.IamportResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class IamportPaymentResponseDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class IamportPaymentPrepareResponseDto{
        private int code;
        private String message;
        private CheckMerchantUid response;

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        public static class CheckMerchantUid {
            @JsonProperty("merchant_uid")
            private String merchantUid;
        }
    }
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class IamportPaymentCheckResponseDto{
        private int code;
        private String message;
        private CheckResponse response;

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        public static class CheckResponse {
            @JsonProperty("amount")
            private int amount;

            @JsonProperty("merchant_uid")
            private String merchantUid;

            @JsonProperty("imp_uid")
            private String impUid;

            @JsonProperty("pg_provider")
            private String pgProvider;
        }
    }

}