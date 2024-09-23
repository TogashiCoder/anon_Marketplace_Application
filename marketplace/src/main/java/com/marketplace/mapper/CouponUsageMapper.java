package com.marketplace.mapper;

import com.marketplace.dto.CouponUsageDto;
import com.marketplace.model.CouponUsage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CouponUsageMapper {
    @Mapping(source = "coupon.id", target = "couponId")
    @Mapping(source = "buyer.id", target = "buyerId")
    @Mapping(source = "product.id", target = "productId")
    CouponUsageDto toDto(CouponUsage couponUsage);

    @Mapping(source = "couponId", target = "coupon.id")
    @Mapping(source = "buyerId", target = "buyer.id")
    @Mapping(source = "productId", target = "product.id")
    CouponUsage toEntity(CouponUsageDto couponUsageDto);
}
