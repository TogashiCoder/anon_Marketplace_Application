package com.marketplace.service;

import com.marketplace.dto.CouponUsageDto;

import java.util.List;

public interface ICouponUsageService {

    CouponUsageDto createCouponUsage(CouponUsageDto couponUsageDto);
    CouponUsageDto getCouponUsageById(Long id);
    List<CouponUsageDto> getAllCouponUsages();
    List<CouponUsageDto> getCouponUsagesByCouponId(Long couponId);
    List<CouponUsageDto> getCouponUsagesByBuyerId(Long buyerId);
    boolean isCouponUsedByBuyer(Long couponId, Long buyerId);
    void markCouponAsUsed(Long couponId, Long buyerId, Long productId);
    void deleteCouponUsage(Long id);
}
