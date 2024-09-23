package com.marketplace.controller;

import com.marketplace.dto.CouponUsageDto;
import com.marketplace.service.ICouponUsageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupon-usages")
@RequiredArgsConstructor
@Validated
public class CouponUsageController {

    private final ICouponUsageService couponUsageService;

    @PostMapping
    public ResponseEntity<CouponUsageDto> createCouponUsage(@Valid @RequestBody CouponUsageDto couponUsageDto) {
        CouponUsageDto createdCouponUsage = couponUsageService.createCouponUsage(couponUsageDto);
        return new ResponseEntity<>(createdCouponUsage, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CouponUsageDto> getCouponUsageById(@PathVariable Long id) {
        CouponUsageDto couponUsageDto = couponUsageService.getCouponUsageById(id);
        return ResponseEntity.ok(couponUsageDto);
    }

    @GetMapping
    public ResponseEntity<List<CouponUsageDto>> getAllCouponUsages() {
        List<CouponUsageDto> couponUsages = couponUsageService.getAllCouponUsages();
        return ResponseEntity.ok(couponUsages);
    }

    @GetMapping("/coupon/{couponId}")
    public ResponseEntity<List<CouponUsageDto>> getCouponUsagesByCouponId(@PathVariable Long couponId) {
        List<CouponUsageDto> couponUsages = couponUsageService.getCouponUsagesByCouponId(couponId);
        return ResponseEntity.ok(couponUsages);
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<CouponUsageDto>> getCouponUsagesByBuyerId(@PathVariable Long buyerId) {
        List<CouponUsageDto> couponUsages = couponUsageService.getCouponUsagesByBuyerId(buyerId);
        return ResponseEntity.ok(couponUsages);
    }



    @PostMapping("/mark-used/{couponId}/{buyerId}/{productId}")
    public ResponseEntity<Void> markCouponAsUsed(
            @PathVariable Long couponId,
            @PathVariable Long buyerId,
            @PathVariable Long productId) {
        couponUsageService.markCouponAsUsed(couponId, buyerId, productId);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCouponUsage(@PathVariable Long id) {
        couponUsageService.deleteCouponUsage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-usage")
    public ResponseEntity<Boolean> isCouponUsedByBuyer(@RequestParam Long couponId, @RequestParam Long buyerId) {
        boolean isUsed = couponUsageService.isCouponUsedByBuyer(couponId, buyerId);
        return ResponseEntity.ok(isUsed);
    }
}
