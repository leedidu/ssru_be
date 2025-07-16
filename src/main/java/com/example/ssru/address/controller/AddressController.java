package com.example.ssru.address.controller;

import com.example.ssru.address.dto.AddressRequestDto;
import com.example.ssru.address.dto.AddressResponseDto;
import com.example.ssru.address.mapper.AddressMapper;
import com.example.ssru.address.service.AddressService;
import com.example.ssru.exception.CustomExceptionCode;
import com.example.ssru.exception.ExceptionResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;
    private final AddressMapper addressMapper;

    @PostMapping
    public ResponseEntity postAddress(@Valid @RequestBody AddressRequestDto.Post post){
        addressService.createAddress(addressMapper.AddressRequestPostDto(post));
        AddressResponseDto.Response response = new AddressResponseDto.Response("success");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getAddress() {
        List<AddressResponseDto.AddressWithId> mappedAddresses = addressService.mapAddress();
        return new ResponseEntity<>(mappedAddresses, HttpStatus.OK);
    }

    @PatchMapping("/{address-id}")
    public ResponseEntity patchAddress(@Positive @PathVariable("address-id") int addressId,
                                    @RequestBody AddressRequestDto.Patch patch){
        boolean check = addressService.patchAddress(addressId, addressMapper.AddressRequestPatchDto(patch));
        System.out.println("주소 수정 결과 : " + check);
        return getResponseEntity(check);
    }


    @DeleteMapping("/{address-id}")
    public ResponseEntity deleteAddress(@Positive @PathVariable("address-id") int addressId){
        boolean check = addressService.deleteAddress(addressId);
        System.out.println("주소 삭제 결과 : " + check);
        return getResponseEntity(check);
    }

    private ResponseEntity getResponseEntity(boolean check) {
        String message = check ? "success" : CustomExceptionCode.ADDRESS_NOT_FOUND.getMessage();
        HttpStatus status = check ? HttpStatus.OK : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(
                check ? new AddressResponseDto.Response(message) : new ExceptionResponseDto.Response("fail", CustomExceptionCode.ADDRESS_NOT_FOUND.getCode(), message),
                status);
    }
}
