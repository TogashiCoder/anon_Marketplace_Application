package com.marketplace.repository;

import com.marketplace.model.Coupon;
import com.marketplace.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByCategoryId(Long categoryId);
    List<Product> findBySellerId(Long sellerId);
    List<Product> findByCouponIsNull();
    List<Product> findByCoupon(Coupon coupon);


    Page<Product> findAll(Pageable pageable);
    @Query("SELECT p FROM Product p ORDER BY SIZE(p.views) DESC")
    List<Product> findMostViewedProducts(Pageable pageable);
    @Query("SELECT p FROM Product p ORDER BY SIZE(p.favorites) DESC")
    List<Product> findMostFavoritedProducts(Pageable pageable);

}
