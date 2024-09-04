package com.marketplace.repository;

import com.marketplace.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Optional<Favorite> findByBuyerIdAndProductId(Long buyerId, Long productId);
    void deleteByBuyerIdAndProductId(Long buyerId, Long productId);
    List<Favorite> findByBuyerId(Long buyerId);
    boolean existsByBuyerIdAndProductId(Long buyerId, Long productId);
}
