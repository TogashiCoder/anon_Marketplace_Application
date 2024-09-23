package com.marketplace.repository;

import com.marketplace.model.CouponUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponUsageRepository extends JpaRepository<CouponUsage, Long> {

    List<CouponUsage> findByCouponId(Long couponId);
    List<CouponUsage> findByBuyerId(Long buyerId);
    boolean existsByCouponIdAndBuyerIdAndUsed(Long couponId, Long buyerId, boolean used);
}
