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

    @PostMapping
    public ResponseEntity<CouponDto> createCoupon(@Valid @RequestBody CouponDto couponDto) {
        CouponDto createdCoupon = couponService.createCoupon(couponDto);
        return new ResponseEntity<>(createdCoupon, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CouponDto> updateCoupon(@PathVariable Long id,@Valid @RequestBody CouponDto couponDto) {
        CouponDto updatedCoupon = couponService.updateCoupon(id, couponDto);
        return new ResponseEntity<>(updatedCoupon, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/code/{code}")
    public ResponseEntity<Void> deleteCouponByCode(@PathVariable String code) {
        couponService.deleteCouponByCode(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponDto> getCouponById(@PathVariable Long id) {
        CouponDto coupon = couponService.getCouponById(id);
        return new ResponseEntity<>(coupon, HttpStatus.OK);
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<CouponDto>> getAllCouponsForASeller(@PathVariable Long sellerId) {
        List<CouponDto> coupons = couponService.getAllCouponsForASeller(sellerId);
        return new ResponseEntity<>(coupons, HttpStatus.OK);
    }

    @PostMapping("/{couponId}/product/{productId}/remove")
    public ResponseEntity<Void> removeCouponFromProduct(@PathVariable Long couponId, @PathVariable Long productId) {
        couponService.removeCouponFromProduct(couponId, productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CouponDto> getCouponByCode(@PathVariable String code) {
        CouponDto coupon = couponService.getCouponByCode(code);
        return new ResponseEntity<>(coupon, HttpStatus.OK);
    }

    @GetMapping("/validate/{couponId}/product/{productId}")
    public ResponseEntity<Boolean> isCouponValid(@PathVariable Long couponId, @PathVariable Long productId) {
        boolean isValid = couponService.isCouponValid(couponId, productId);
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }

    @PostMapping("/{couponId}/apply/product/{productId}/buyer/{buyerId}")
    public ResponseEntity<CouponDto> applyCouponToProduct(
            @PathVariable Long couponId,
            @PathVariable Long productId,
            @PathVariable Long buyerId) {
        CouponDto appliedCoupon = couponService.applyCouponToProduct(couponId, productId, buyerId);
        return new ResponseEntity<>(appliedCoupon, HttpStatus.OK);
    }
}
