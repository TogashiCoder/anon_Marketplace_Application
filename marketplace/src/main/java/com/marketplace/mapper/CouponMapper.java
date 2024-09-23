package com.marketplace.mapper;

import com.marketplace.dto.CouponDto;
import com.marketplace.model.Coupon;
import com.marketplace.model.CouponUsage;
import com.marketplace.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CouponMapper {

    public CouponDto toDto(Coupon coupon) {
        CouponDto dto = new CouponDto();
        dto.setId(coupon.getId());
        dto.setCode(coupon.getCode());
        dto.setDiscountPercentage(coupon.getDiscountPercentage());
        dto.setStartDate(coupon.getStartDate());
        dto.setEndDate(coupon.getEndDate());
        dto.setMaxRedemptions(coupon.getMaxRedemptions());
        dto.setRedeemCount(coupon.getRedeemCount());
        dto.setProductIds(coupon.getProducts().stream()
                .map(Product::getId)
                .collect(Collectors.toList()));
        dto.setCouponUsages(coupon.getCouponUsages());
        return dto;
    }

    public Coupon toEntity(CouponDto dto) {
        Coupon coupon = new Coupon();
        coupon.setId(dto.getId());
        coupon.setCode(dto.getCode());
        coupon.setDiscountPercentage(dto.getDiscountPercentage());
        coupon.setStartDate(dto.getStartDate());
        coupon.setEndDate(dto.getEndDate());
        coupon.setMaxRedemptions(dto.getMaxRedemptions());
        coupon.setRedeemCount(dto.getRedeemCount());
        // Note: Products and CouponUsages are typically managed separately
        return coupon;
    }

    public void updateEntityFromDto(CouponDto dto, Coupon coupon) {
        coupon.setCode(dto.getCode());
        coupon.setDiscountPercentage(dto.getDiscountPercentage());
        coupon.setStartDate(dto.getStartDate());
        coupon.setEndDate(dto.getEndDate());
        coupon.setMaxRedemptions(dto.getMaxRedemptions());
        // Note: RedeemCount is typically managed by the service layer
        // Products and CouponUsages are typically managed separately
    }
}