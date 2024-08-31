package com.marketplace.repository;

import com.marketplace.model.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {
    List<Follower> findByBuyerId(Long buyerId);
    List<Follower> findBySellerId(Long sellerId);
    Optional<Follower> findByBuyerIdAndSellerId(Long buyerId, Long sellerId);
    boolean existsByBuyerIdAndSellerId(Long buyerId, Long sellerId);
    long countBySellerId(Long sellerId);
}