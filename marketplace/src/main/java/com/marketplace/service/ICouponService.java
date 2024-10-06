package com.marketplace.service;

import com.marketplace.dto.CouponDto;
import com.marketplace.model.Coupon;

import java.util.List;

public interface ICouponService {

    // Create a new coupon for a seller
    CouponDto createCoupon(CouponDto couponDto, Long sellerId);

    // Update an existing coupon
    CouponDto updateCoupon(Long id, CouponDto couponDto);

    // Get a list of coupons by seller ID
    List<CouponDto> getAllCouponsBySeller(Long sellerId);

    // Apply a coupon to a product for a specific buyer
    CouponDto applyCouponToProduct(Long couponId, Long productId, Long buyerId);

    // Remove a coupon from a product
    void removeCouponFromProduct(Long productId);

    // Delete a coupon by its ID
    void deleteCoupon(Long id);

    // Delete a coupon by its code
    void deleteCouponByCode(String code);

    // Get a coupon by its ID
    CouponDto getCouponById(Long id);

    // Get a coupon by its code
    CouponDto getCouponByCode(String code);

    // Check if a coupon is valid for a specific product
    boolean isCouponValid(Long couponId, Long productId);

    // Check if a coupon code is already in use
    boolean isCouponCodeAlreadyUsed(String code);

    // Remove a coupon from a cart item
    void removeCouponFromCartItem(Long cartItemId);

}
