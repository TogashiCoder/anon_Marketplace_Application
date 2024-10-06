package com.marketplace.repository;

import com.marketplace.model.ProductView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductViewRepository extends JpaRepository<ProductView, Long> {

    long countByProductId(Long productId);
    long countByProductIdAndViewedAtBetween(Long productId, LocalDateTime start, LocalDateTime end);
    List<ProductView> findByProductIdOrderByViewedAtDesc(Long productId);




}
