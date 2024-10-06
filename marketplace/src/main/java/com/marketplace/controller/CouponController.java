package com.marketplace.controller;

import com.marketplace.dto.CouponDto;
import com.marketplace.service.ICouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Validated
public class CouponController {

    private final ICouponService couponService;

    // Create a coupon for a specific seller
    @PostMapping("/seller/{sellerId}")
    public ResponseEntity<CouponDto> createCoupon(@PathVariable Long sellerId, @Valid @RequestBody CouponDto couponDto) {
        CouponDto createdCoupon = couponService.createCoupon(couponDto, sellerId);
        return new ResponseEntity<>(createdCoupon, HttpStatus.CREATED);
    }

    // Update an existing coupon by coupon ID
    @PutMapping("/{id}")
    public ResponseEntity<CouponDto> updateCoupon(@PathVariable Long id, @Valid @RequestBody CouponDto couponDto) {
        CouponDto updatedCoupon = couponService.updateCoupon(id, couponDto);
        return new ResponseEntity<>(updatedCoupon, HttpStatus.OK);
    }

    // Get all coupons for a specific seller
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<CouponDto>> getAllCouponsBySeller(@PathVariable Long sellerId) {
        List<CouponDto> coupons = couponService.getAllCouponsBySeller(sellerId);
        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }

    // Apply a coupon to a product for a specific buyer
    @PostMapping("/{couponId}/apply/{productId}/buyer/{buyerId}")
    public ResponseEntity<CouponDto> applyCouponToProduct(@PathVariable Long couponId, @PathVariable Long productId, @PathVariable Long buyerId) {
        CouponDto appliedCoupon = couponService.applyCouponToProduct(couponId, productId, buyerId);
        return new ResponseEntity<>(appliedCoupon, HttpStatus.OK);
    }

    // Remove a coupon from a product
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> removeCouponFromProduct(@PathVariable Long productId) {
        couponService.removeCouponFromProduct(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Delete a coupon by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Delete a coupon by code
    @DeleteMapping("/code/{code}")
    public ResponseEntity<Void> deleteCouponByCode(@PathVariable String code) {
        couponService.deleteCouponByCode(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Get a coupon by ID
    @GetMapping("/{id}")
    public ResponseEntity<CouponDto> getCouponById(@PathVariable Long id) {
        CouponDto coupon = couponService.getCouponById(id);
        return new ResponseEntity<>(coupon, HttpStatus.OK);
    }

    // Get a coupon by code
    @GetMapping("/code/{code}")
    public ResponseEntity<CouponDto> getCouponByCode(@PathVariable String code) {
        CouponDto coupon = couponService.getCouponByCode(code);
        return new ResponseEntity<>(coupon, HttpStatus.OK);
    }

    // Remove a coupon from a cart item
    @DeleteMapping("/cartItem/{cartItemId}")
    public ResponseEntity<Void> removeCouponFromCartItem(@PathVariable Long cartItemId) {
        couponService.removeCouponFromCartItem(cartItemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
