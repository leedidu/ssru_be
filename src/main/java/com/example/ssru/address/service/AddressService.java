package com.example.ssru.address.service;

import com.example.ssru.address.dto.AddressResponseDto;
import com.example.ssru.address.entity.Address;
import com.example.ssru.address.repository.AddressRepository;
import com.example.ssru.user.entity.User;
import com.example.ssru.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AddressService {

    private AddressRepository addressRepository;
    private UserService userService;

    public Address createAddress(Address address){
        User user = userService.getLoginUser();
        address.setUser(user);
        System.out.println("----- 주소 등록 성공 -----");
        return addressRepository.save(address);
    }

    public List<Address> getUserAddress(){
        User user = userService.getLoginUser();
        System.out.println("----- 주소 목록 반환 성공 -----");
        return addressRepository.findAddressByUser(user);
    }

    public List<AddressResponseDto.AddressWithId> mapAddress(){
        List<Address> addressList = getUserAddress();
        return addressList.stream()
                .map(address -> new AddressResponseDto.AddressWithId(
                        address.getId(),
                        new AddressResponseDto.AddressDetail(
                                address.getAddress(),
                                address.getDetailAddress(),
                                address.getType()
                        )
                ))
                .collect(Collectors.toList());
    }

    public boolean patchAddress(int addressId, Address updatedAddress){
        System.out.println("----- 주소 수정 시작 -----");
        return Optional.ofNullable(verifiedAddress(addressId))
                .map(existingAddress -> {
                    Optional.ofNullable(updatedAddress.getAddress()).ifPresent(existingAddress::setAddress);
                    Optional.ofNullable(updatedAddress.getDetailAddress()).ifPresent(existingAddress::setDetailAddress);
                    Optional.ofNullable(updatedAddress.getType()).ifPresent(existingAddress::setType);
                    addressRepository.save(existingAddress);
                    return true;
                })
                .orElse(false);
    }

    public Address verifiedAddress(int optionId){
        Optional<Address> option = addressRepository.findById(optionId);
        return option.orElse(null);
    }

    public boolean deleteAddress(int optionId){
        System.out.println("----- 주소 삭제 시작 -----");
        return Optional.ofNullable(verifiedAddress(optionId))
                .map(address -> {
                    addressRepository.delete(address);
                    return true;
                })
                .orElse(false);
    }
}
