package com.example.ssru.address.mapper;

import com.example.ssru.address.dto.AddressRequestDto;
import com.example.ssru.address.dto.AddressResponseDto;
import com.example.ssru.address.entity.Address;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressResponseDto.Response AddressResponseDto(Address address);
    Address AddressRequestPostDto(AddressRequestDto.Post post);
    Address AddressRequestPatchDto(AddressRequestDto.Patch patch);
    AddressResponseDto.AddressDetail AddressDetailResponseDto(Address address);
    default AddressResponseDto.AddressList AddressListResponseDto(List<AddressResponseDto.AddressWithId> addressList) {
        return new AddressResponseDto.AddressList(addressList);
    }

    default AddressResponseDto.AddressListWithId AddressListWithIdResponseDto(List<Address> addressList) {
        List<AddressResponseDto.AddressWithId> mappedAddresses = addressList.stream()
                .map(this::AddressWithIdResponseDto)
                .collect(Collectors.toList());

        return new AddressResponseDto.AddressListWithId(mappedAddresses);
    }

    default AddressResponseDto.AddressWithId AddressWithIdResponseDto(Address address) {
        return new AddressResponseDto.AddressWithId(
                address.getId(),
                address.getAddress(),
                address.getDetailAddress(),
                address.getType()
        );
    }
}