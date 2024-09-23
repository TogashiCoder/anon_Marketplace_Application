package com.marketplace.repository;

import com.marketplace.model.Coupon;
import com.marketplace.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {


    // Find all coupons for a specific seller
    List<Coupon> findByProducts_Seller_Id(Long sellerId);
    boolean existsByCode(String code);
    // Find a coupon by its code
    Optional<Coupon> findByCode(String code);


}
