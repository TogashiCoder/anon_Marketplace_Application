package com.marketplace.repository;

import com.marketplace.model.Buyer;
import com.marketplace.model.Coupon;
import com.marketplace.model.CouponUsage;
import com.marketplace.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponUsageRepository extends JpaRepository<CouponUsage, Long> {

    List<CouponUsage> findByCouponId(Long couponId);
    List<CouponUsage> findByBuyerId(Long buyerId);
    boolean existsByCouponIdAndBuyerIdAndUsed(Long couponId, Long buyerId, boolean used);
    long countByCouponId(Long couponId);
    boolean existsByCouponAndBuyerAndProduct(Coupon coupon, Buyer buyer, Product product);

}
