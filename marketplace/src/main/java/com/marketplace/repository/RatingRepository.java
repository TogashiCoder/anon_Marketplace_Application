package com.marketplace.repository;

import com.marketplace.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository  extends JpaRepository<Rating, Long> {
    List<Rating> findAllByProductId(Long productId);
    List<Rating> findAllBySellerId(Long sellerId);

    Long countByProductId(Long productId);
    Long countBySellerId(Long sellerId);
}