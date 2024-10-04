package com.marketplace.service;

import com.marketplace.dto.CouponDto;
import com.marketplace.model.Coupon;

import java.util.List;

public interface ICouponService {

    CouponDto createCoupon(CouponDto couponDto);
    CouponDto updateCoupon(Long id, CouponDto couponDto);
    void deleteCoupon(Long id);
    void deleteCouponByCode(String code);
    CouponDto getCouponById(Long id);
    CouponDto getCouponByCode(String code);
    boolean isCouponValid(Long couponId, Long productId);
    boolean isCouponCodeAlreadyUsed(String code);
    CouponDto applyCouponToProduct(Long couponId, Long productId,Long buyerId);
    List<CouponDto> getAllCouponsForASeller(Long sellerId);
    void removeCouponFromProduct(Long couponId, Long productId);


}
