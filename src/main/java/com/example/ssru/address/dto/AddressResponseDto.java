package com.example.ssru.address.dto;

import com.example.ssru.address.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public class AddressResponseDto {
    @Getter
    @AllArgsConstructor
    public static class Response{
        private String status;
    }

    @Getter
    @AllArgsConstructor
    public static class AddressList{
        private List<AddressWithId> addressList;
    }

    @Getter
    @AllArgsConstructor
    public static class AddressListWithId {
        private List<AddressWithId> addressList;
    }


    @Getter
    @AllArgsConstructor
    public static class AddressWithId{
        private int id;
        private AddressDetail addressDetail;

        public AddressWithId(int id, String address, String detailAddress, String type) {
            this.id = id;
            this.addressDetail = new AddressDetail(address, detailAddress, type);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class AddressDetail{
        private String address;
        private String detailAddress;
        private String type;
    }
}
