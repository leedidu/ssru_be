package com.example.ssru.coupon.controller;

import com.example.ssru.coupon.dto.CouponRequestDto;
import com.example.ssru.coupon.dto.CouponResponseDto;
import com.example.ssru.coupon.entity.Coupon;
import com.example.ssru.coupon.mapper.CouponMapper;
import com.example.ssru.coupon.service.CouponService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/coupon")
public class CouponController {

    private final CouponService couponService;
    private final CouponMapper couponMapper;

    @PostMapping
    public ResponseEntity postCoupon(@Valid @RequestBody CouponRequestDto.Post post){
        Coupon coupon = couponService.createCoupon(couponMapper.CouponRequestPostDto(post));
        CouponResponseDto.Response response = couponMapper.CouponResponseDto(coupon);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getCoupon(@Positive @RequestParam int couponId){
        Coupon coupon = couponService.findCoupon(couponId);
        CouponResponseDto.Response response = couponMapper.CouponResponseDto(coupon);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{coupon-id}")
    public ResponseEntity patchCoupon(@Positive @PathVariable("coupon-id") int couponId,
                                    @RequestBody CouponRequestDto.Patch patch){
        patch.setId(couponId);
        Coupon coupon = couponService.patchCoupon(couponMapper.CouponRequestPatchDto(patch));
        CouponResponseDto.Response response = couponMapper.CouponResponseDto(coupon);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{coupon-id}")
    public ResponseEntity deleteCoupon(@Positive @PathVariable("coupon-id") int couponId){
        couponService.deleteCoupon(couponId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
