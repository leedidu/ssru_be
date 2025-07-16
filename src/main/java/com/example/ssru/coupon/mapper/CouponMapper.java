package com.example.ssru.coupon.mapper;

import com.example.ssru.coupon.dto.CouponRequestDto;
import com.example.ssru.coupon.dto.CouponResponseDto;
import com.example.ssru.coupon.entity.Coupon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CouponMapper {
    CouponResponseDto.Response CouponResponseDto(Coupon coupon);

    Coupon CouponRequestPostDto(CouponRequestDto.Post post);

    Coupon CouponRequestPatchDto(CouponRequestDto.Patch patch);

    CouponResponseDto.CouponDetail toCouponDetail(Coupon coupon);
}
