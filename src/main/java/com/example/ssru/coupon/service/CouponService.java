package com.example.ssru.coupon.service;

import com.example.ssru.coupon.entity.Coupon;
import com.example.ssru.coupon.repository.CouponRepository;
import com.example.ssru.exception.CustomException;
import com.example.ssru.exception.CustomExceptionCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CouponService {

    private CouponRepository couponRepository;

    public Coupon createCoupon(Coupon coupon){
        return couponRepository.save(coupon);
    }

    public Coupon findCoupon(int couponId){
        return verifiedCoupon(couponId);
    }

    public Coupon patchCoupon(Coupon coupon){
        Coupon coupon1 = findCoupon(coupon.getId());
        Optional.ofNullable(coupon.getWhetherUse()).ifPresent(coupon1::setWhetherUse);
        Optional.ofNullable(coupon.getCouponName()).ifPresent(coupon1::setCouponName);
        Optional.ofNullable(coupon.getPublish()).ifPresent(coupon1::setPublish);
        Optional.ofNullable(coupon.getDeadline()).ifPresent(coupon1::setDeadline);
        if(coupon.getDiscount() != 0) coupon1.setDiscount(coupon.getDiscount());
        return couponRepository.save(coupon1);
    }

    public Coupon verifiedCoupon(int couponId){
        Optional<Coupon> coupon = couponRepository.findById(couponId);
        return coupon.orElseThrow(() -> new CustomException(CustomExceptionCode.SSTEAL_HISTORY_NOT_FOUNT));
    }

    public void deleteCoupon(int couponId){
        Coupon coupon1 = verifiedCoupon(couponId);
        couponRepository.delete(coupon1);
    }
}
